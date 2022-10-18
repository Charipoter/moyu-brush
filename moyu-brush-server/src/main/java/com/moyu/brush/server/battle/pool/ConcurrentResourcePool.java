package com.moyu.brush.server.battle.pool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;

/**
 * 参考 hikariCp 连接池
 * 每一个 entry 都看作一种资源，应当是一个状态机，并可以检测人员是否符合条件
 * 该池中的节点满足规范即可
 */
@Slf4j
public class ConcurrentResourcePool<T extends PoolEntry> implements AutoCloseable {
    // 存放所有房间
    private final CopyOnWriteArrayList<T> sharedList;
    // 线程本地存放的资源是否为弱引用（如若为弱引用，则无需担心内存泄露）
    private final boolean weakThreadLocals;
    // 线程本地放一些房间
    private final ThreadLocal<List<Object>> threadList;
    // 监听器
    private final PoolListener listener;
    // 记录等待者数量
    private final AtomicInteger waiters;
    // 关闭状态
    private volatile boolean closed;
    // 存放新创建的房间，优先给予等待者，但可能会被偷窃，注意该队列的特性
    private final SynchronousQueue<T> handoffQueue;

    private final Factor<T> defaultFactor = new NoopFactor<>();


    public ConcurrentResourcePool(final PoolListener listener) {
        this.listener = listener;
        this.weakThreadLocals = useWeakThreadLocals();

        this.handoffQueue = new SynchronousQueue<>(true);
        this.waiters = new AtomicInteger();
        this.sharedList = new CopyOnWriteArrayList<>();

        this.threadList = ThreadLocal.withInitial(() -> new ArrayList<>(16));

    }



    /**
     * 从池中获取一份可用资源，等待特定时间
     */
    public T borrow(long timeout, final TimeUnit timeUnit, @Nullable Factor<T> factor) throws InterruptedException {
        if (factor == null) {
            factor = defaultFactor;
        }
        // 从线程本地获取，依旧存在并发
        final List<Object> list = threadList.get();
        for (int i = list.size() - 1; i >= 0; i--) {
            final Object entry = list.remove(i);
            @SuppressWarnings("unchecked") final T resource = weakThreadLocals ? ((WeakReference<T>) entry).get() : (T) entry;

            if (resource != null && factor.canAcquire(resource) && resource.acquire()) {

                return resource;
            }
        }

        // 从共享集合获取
        final int waiting = waiters.incrementAndGet();
        try {
            for (T resource : sharedList) {
                if (factor.canAcquire(resource) && resource.acquire()) {
                    // 可能窃取了等待者的资源，需要判断一下
                    // 如若存在非自己的等待者，并且当前线程获取的资源已耗尽，则需要再创建一份资源
                    if (waiting > 1 && resource.exhaust()) {
                        listener.addResource(waiting - 1);
                    }
                    return resource;
                }
            }

            listener.addResource(waiting);

            // 高精度时间判定
            timeout = timeUnit.toNanos(timeout);
            do {
                final long start = System.currentTimeMillis();
                final T resource = handoffQueue.poll(timeout, NANOSECONDS);
                if (resource == null || (factor.canAcquire(resource) && resource.acquire())) {
                    return resource;
                }

                timeout -= timeUnit.toNanos(System.currentTimeMillis() - start);
            } while (timeout > 10_000);

            return null;
        } finally {
            waiters.decrementAndGet();
        }
    }

    /**
     * 返还一份资源
     */
    public void requite(final T entry) {
        // 默认释放一份资源
        entry.release(1);
        // 如若存在等待者，则尝试将资源放入传递队列
        for (int i = 0; waiters.get() > 0; i++) {

            if (!entry.exhaust() || handoffQueue.offer(entry)) {
                // 如若资源被耗尽了或者成功放入队列，返回
                return;
            } else if ((i & 0xff) == 0xff) {
                parkNanos(MICROSECONDS.toNanos(10));
            } else {
                Thread.yield();
            }
        }
        // 往线程本地放一份
        final List<Object> threadLocalList = threadList.get();
        if (threadLocalList.size() < 50) {
            threadLocalList.add(weakThreadLocals ? new WeakReference<>(entry) : entry);
        }
    }

    /**
     * 添加一个资源
     */
    public void add(final T resource) {
        if (closed) {
            log.info("资源池已关闭，忽略 add()");
            throw new IllegalStateException("资源池已关闭，忽略 add()");
        }

        sharedList.add(resource);

        // 如若有等待者，自旋尝试耗尽资源或者成功放入队列
        while (waiters.get() > 0 && !resource.exhaust() && !handoffQueue.offer(resource)) {
            Thread.yield();
        }
    }

    /**
     * 移除一个资源，被移除的资源必须是耗尽的或者不可用的
     */
    public boolean remove(final T resource) {
        if (!resource.remove() && !closed) {
            log.warn("尝试移除一个未耗尽或者可用的资源: {}", resource);
            return false;
        }

        final boolean removed = sharedList.remove(resource);
        if (!removed && !closed) {
            log.warn("尝试移除一个不存在的资源: {}", resource);
        }

        threadList.get().remove(resource);

        return removed;
    }

    /**
     * 关闭池
     */
    @Override
    public void close() {
        closed = true;
    }

    /**
     * 该方法提供了指定状态的时间的“快照”。它不会以任何方式“锁定”或保留项目。在
     * 对列表中的项目执行该操作之前最好调用 reserve(T) 进行保留。
     */
    public List<T> values(final int state) {
        final List<T> list = sharedList.stream().filter(e -> e.getState() == state).collect(Collectors.toList());
        Collections.reverse(list);
        return list;
    }

    /**
     * 拷贝一份资源快照
     */
    @SuppressWarnings("unchecked")
    public List<T> values() {
        return (List<T>) sharedList.clone();
    }

    /**
     * 保留一个资源，让其不可用，可用来保存一个快照
     */
    public boolean reserve(final T resource) {
        // 我们需要保证该资源没有被人获取，否则这里快照没什么意义
        return resource.reserve();
    }

    /**
     * 让一个不可用资源重新可用
     */
    public void unReserve(final T resource) {
        if (resource.unReserve()) {
            // 自旋给出资源
            while (waiters.get() > 0 && !handoffQueue.offer(resource)) {
                Thread.yield();
            }
        } else {
            log.warn("尝试移除一个不存在的资源: {}", resource);
        }
    }

    /**
     * 获取等待者数量
     */
    public int getWaitingThreadCount() {
        return waiters.get();
    }

    /**
     * 获取特定状态的资源数
     */
    public int getCount(final int state) {
        int count = 0;
        for (PoolEntry e : sharedList) {
            if (e.getState() == state) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取所有资源数
     */
    public int size() {
        return sharedList.size();
    }

    public void dumpState() {
        sharedList.forEach(entry -> log.info(entry.toString()));
    }

    /**
     * 判断是否要使用弱引用值
     */
    private boolean useWeakThreadLocals() {
//        return getClass().getClassLoader() != ClassLoader.getSystemClassLoader();
        return true;
    }

}
