package com.moyu.brush.server.battle;

import com.moyu.brush.server.battle.pool.AbstractPoolEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线上对战的一个房间
 */
@Getter
@Setter
@Slf4j
public class BattleRoom extends AbstractPoolEntry {
    /**
     * 房间 id
     */
    private String id;
    /**
     * 记录房间内的人员，以 id 标识
     */
    private Map<String, Session> memberMap;

    public BattleRoom(int roomSize) {
        super(roomSize);
        this.memberMap = new ConcurrentHashMap<>();
        this.id = UUID.randomUUID().toString();
    }

    public void join(Session session, String userId) {
        memberMap.put(userId, session);
        // 广播
        broadcast(userId, "用户 %s 加入了房间".formatted(userId));
    }

    public void exit(Session session, String userId) {
        memberMap.remove(userId);
        // 广播
        broadcast(userId, "用户 %s 退出了房间".formatted(userId));
    }

    public void exchange(Session session, String userId, String message) {
        broadcast(userId, message);
    }

    private void broadcast(String userId, String message) {
        // 注意当调用到某个 session 时，其可能已被关闭
        for (Session session : memberMap.values()) {
            sendTo(session, message);
        }
    }

    private void sendTo(Session session, String message) {
        // 需要加锁，否则会存在多个线程同时调用 session 发消息的情况，导致报错
        synchronized (session) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "room{%s}".formatted(id);
    }
}
