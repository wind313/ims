package com.yjc.server.websocket.server;

public interface IMServer {
    boolean isReady();
    void start();
    void stop();
}
