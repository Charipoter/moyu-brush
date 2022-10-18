package com.moyu.brush.server.battle.pool;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AbstractPoolEntry implements PoolEntry {

    private static final int REMOVED = -1;
    private static final int UNAVAILABLE = -2;
    private static final int AVAILABLE = 1;

    private volatile int state = AVAILABLE;

    private final Semaphore resource;

    private static final AtomicIntegerFieldUpdater<AbstractPoolEntry> STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(AbstractPoolEntry.class, "state");
    // 资源总量
    private final int amount;

    protected AbstractPoolEntry(int amount) {
        this.resource = new Semaphore(amount);
        this.amount = amount;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public boolean acquire() {
        // 判断资源是否可以获取
        if (state == UNAVAILABLE || state == REMOVED) {
            return false;
        }

        return resource.tryAcquire();
    }

    @Override
    public boolean exhaust() {
        return resource.availablePermits() == 0;
    }

    @Override
    public void release(int r) {
        resource.release(r);
    }

    private boolean inUse() {
        return resource.availablePermits() < amount;
    }

    @Override
    public boolean remove() {
        // 未被持有才可以删除
        if (inUse()) {
            return false;
        }
        if (state == AVAILABLE) {
            casState(AVAILABLE, REMOVED);
        } else if (state == UNAVAILABLE) {
            casState(UNAVAILABLE, REMOVED);
        }
        return state == REMOVED;
    }

    @Override
    public boolean reserve() {
        if (inUse() && state == REMOVED) {
            return false;
        }
        casState(AVAILABLE, UNAVAILABLE);
        return state == UNAVAILABLE;
    }

    @Override
    public boolean unReserve() {
        if (state != UNAVAILABLE) {
            return false;
        }
        casState(UNAVAILABLE, AVAILABLE);
        return state == AVAILABLE;
    }

    private boolean casState(int expectState, int newState) {
        return STATE_UPDATER.compareAndSet(this, expectState, newState);
    }
}
