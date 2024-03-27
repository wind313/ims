package com.yjc.server.websocket;

import org.springframework.stereotype.Component;

@Component
public class ServerGroup {
    public static volatile long serverId = 0;
}
