package com.yjc.client.listener;

import com.yjc.client.annotation.Listener;
import com.yjc.common.enums.ListenerType;
import com.yjc.common.model.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageListenerMulticaster {

    @Autowired(required = false)
    private List<MessageListener> messageListeners;

    public void multicast(ListenerType type, ResultInfo resultInfo){
        for(MessageListener messageListener:messageListeners){
            Listener annotation = messageListener.getClass().getAnnotation(Listener.class);
            if (annotation != null && (annotation.type().equals(ListenerType.ALL) || annotation.type().equals(type))){
                messageListener.process(resultInfo);
            }
        }
    }
}
