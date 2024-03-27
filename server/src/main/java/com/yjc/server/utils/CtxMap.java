package com.yjc.server.utils;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CtxMap {
    private static Map<Long, ChannelHandlerContext> contextMap = new ConcurrentHashMap<>();
    public static void add(Long userId,ChannelHandlerContext context){
        if(userId!=null) contextMap.put(userId,context);
    }
    public static void remove(Long userId){
        if(userId != null) contextMap.remove(userId);
    }
    public static ChannelHandlerContext get(Long userId){
        if(userId == null) return null;
        else return contextMap.get(userId);
    }
}
