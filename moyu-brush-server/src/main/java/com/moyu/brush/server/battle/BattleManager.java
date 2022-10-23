package com.moyu.brush.server.battle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线上对战的协调中心
 * 每个全双工连接都对应一个实例，因此字段需要考虑作用域
 */
@ServerEndpoint(value = "/battle")
@Component
@Slf4j
public class BattleManager {

    /**
     * 参考 hikariCp 设计 battleRoomPool
     */
    public static final BattleRoomPool battleRoomPool = new BattleRoomPool();

    public static final Map<String, BattleRoom> userInWhichRoom = new ConcurrentHashMap<>();

    // TODO: 有并发问题，连接关闭会出 bug
    // Message will not be sent because the WebSocket session has been closed
    // 每一个 session 对应一个当前实例，
    private Session session;

    /**
     * 连接建立成功
     */
    @OnOpen
    public void onOpen(Session session) {

        if (this.session != null) {
            log.warn("连接初始建立却已经存在 session");
        }
        this.session = session;
        // 新来了一个用户，分配房间
        BattleRoom battleRoom = distributeBattleRoom();

        battleRoom.join(session, session.getId());
    }

    private BattleRoom distributeBattleRoom() {
        BattleRoom battleRoom = battleRoomPool.getBattleRoom(12000000);

        if (battleRoom != null) {
            userInWhichRoom.put(session.getId(), battleRoom);
            return battleRoom;
        } else {
            throw new RuntimeException(session + "获取房间失败");
        }
    }

    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose() {
        // 找到房间
        String userId = session.getId();
        BattleRoom battleRoom = userInWhichRoom.remove(userId);

        if (battleRoom != null) {
            battleRoom.exit(session, session.getId());
            battleRoomPool.recycle(battleRoom);
        }
    }
    /**
     * 收到客户端消息
     */
    @OnMessage
    public void onMessage(String message) {
        String userId = session.getId();
        BattleRoom room = userInWhichRoom.get(userId);
        if (room != null) {
            room.exchange(session, userId, session.getId() + "：" + message);
        }
    }
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
