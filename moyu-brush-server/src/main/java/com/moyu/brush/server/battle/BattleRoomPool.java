package com.moyu.brush.server.battle;

import com.moyu.brush.server.battle.pool.ConcurrentResourcePool;
import com.moyu.brush.server.battle.pool.PoolListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class BattleRoomPool implements PoolListener {

    private final ConcurrentResourcePool<BattleRoom> pool;

    private final ThreadPoolExecutor addConnectionExecutor;

    private final int defaultRoomSize = 10;

    public BattleRoomPool() {
        this.pool = new ConcurrentResourcePool<>(this);
        LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue<>(10);
        this.addConnectionExecutor = createThreadPoolExecutor(
                addConnectionQueue, "battle room" + "adder",
                null, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    @Override
    public void addResource(int waiting) {
        // waiting 后续再考虑如何使用
        // 重要！！：此处必须异步创建，否则会一直阻塞，这是由 SynchronousQueue 的特性（当且仅当有线程获取时才插入成功）
        addConnectionExecutor.submit(() -> {
            BattleRoom battleRoom = new BattleRoom(defaultRoomSize);
            pool.add(battleRoom);
        });
    }

    public BattleRoom getBattleRoom(int timeout) {
        return getBattleRoom(timeout, null);
    }

    public BattleRoom getBattleRoom(int timeout, BattleFactor factor) {

        final long startTime = System.currentTimeMillis();

        try {
            BattleRoom room = pool.borrow(timeout, TimeUnit.MICROSECONDS, factor);

            final long now = System.currentTimeMillis();
            log.info("花费 {} 毫秒获取了房间：{}", now - startTime, room);

            return room;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("线程在获取房间时中断");
        }
    }

    /**
     * 回收房间，留作下次使用
     */
    public void recycle(BattleRoom battleRoom) {
        pool.requite(battleRoom);
    }

    /**
     * 获取房间数量
     */
    public int battleRoomSize() {
        return pool.size();
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(final BlockingQueue<Runnable> queue, final String threadName, ThreadFactory threadFactory, final RejectedExecutionHandler policy)
    {
        if (threadFactory == null) {
            threadFactory = new DefaultThreadFactory(threadName, true);
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5 /*core*/, 5 /*max*/, 5 /*keepalive*/, SECONDS, queue, threadFactory, policy);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public static final class DefaultThreadFactory implements ThreadFactory {

        private final String threadName;
        private final boolean daemon;

        public DefaultThreadFactory(String threadName, boolean daemon) {
            this.threadName = threadName;
            this.daemon = daemon;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, threadName);
            thread.setDaemon(daemon);
            return thread;
        }
    }


}
