package com.moyu.brush.server.battle;

import com.moyu.brush.server.battle.pool.Factor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 持有用户信息，判断是否能进入房间
 */
@AllArgsConstructor
@NoArgsConstructor
public class BattleFactor implements Factor<BattleRoom> {

    private String userId;
    @Override
    public boolean canAcquire(BattleRoom resource) {
        return !resource.getMemberMap().containsKey(userId);
    }
}
