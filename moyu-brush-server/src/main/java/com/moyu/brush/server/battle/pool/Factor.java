package com.moyu.brush.server.battle.pool;

/**
 * 业务相关的要素，用于扩展判断是否可以获取资源
 */
public interface Factor<T extends PoolEntry> {

    /**
     * 判断资源能否获取
     */
    boolean canAcquire(T resource);

}
