package com.moyu.brush.server.battle.pool;

/**
 * 资源池内的节点，需要是一个状态机
 * 状态机轮转：
 *      刚创建时 -> 资源未被使用
 *      被获取过了 -> 资源有在使用
 *      获取了所有 -> 耗尽
 *
 *      资源未被使用并且被移除了，等待清理 -> 移除
 *      资源未被使用并且被标为不可用了 -> 不可用
 *
 */
public interface PoolEntry {
    int getState();
    /**
     * 判断能否获取，如果获取了进行状态变化
     */
    boolean acquire();
    /**
     * 判断资源是否耗尽
     */
    boolean exhaust();
    /**
     * 返还指定资源数
     */
    void release(int r);
    /**
     * 尝试移除该资源
     */
    boolean remove();
    /**
     * 尝试让资源不可用
     */
    boolean reserve();
    /**
     * 尝试让资源可用
     */
    boolean unReserve();


}