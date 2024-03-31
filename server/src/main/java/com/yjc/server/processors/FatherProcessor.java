package com.yjc.server.processors;

import io.netty.channel.ChannelHandlerContext;

public class FatherProcessor<T> {
    public void process(ChannelHandlerContext ctx,T data){}

    public void process(T data){}

    public T transform(Object o){
        return (T)o;
    }
}
