package com.moyu.brush.server.controller;

import com.moyu.brush.server.battle.BattleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user")
    public Object getUserCount() {
        return BattleManager.userInWhichRoom.size();
    }

    @GetMapping("/room")
    public Object getRoom() {
        return BattleManager.battleRoomPool.battleRoomSize();
    }

    @GetMapping("/baidu")
    public Object call() {
        return restTemplate.getForObject("https://www.baidu.com", String.class);
    }

}
