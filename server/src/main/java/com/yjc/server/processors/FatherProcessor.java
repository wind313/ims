package com.yjc.server.processors;

import io.netty.channel.ChannelHandlerContext;

public class FatherProcessor<T> {
    public void process(ChannelHandlerContext ctx,T data){}
    public T tranForm(Object o){
        return (T)o;
    }
}
