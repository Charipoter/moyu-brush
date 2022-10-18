package com.moyu.brush.server.battle.pool;

public class NoopFactor<T extends PoolEntry> implements Factor<T> {
    @Override
    public boolean canAcquire(T resource) {
        return true;
    }
}
