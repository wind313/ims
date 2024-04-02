package com.yjc.server.websocket;

import com.yjc.common.constant.RedisKey;
import com.yjc.server.websocket.server.IMServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ServerGroup implements CommandLineRunner {
    public static volatile long serverId = 0;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private IMServer server;

    public boolean isReady(){
        return server.isReady();
    }


    @Override
    public void run(String... args) throws Exception {
        String key = RedisKey.MAX_SERVER_ID;
        serverId = redisTemplate.opsForValue().increment(key,1);
        server.start();
    }
}
