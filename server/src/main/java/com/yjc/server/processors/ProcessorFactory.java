package com.yjc.server.processors;

import com.yjc.common.enums.CommandType;
import com.yjc.server.utils.SpringContextHolder;

public class ProcessorFactory {
    public static AbstractProcessor createProcessor(CommandType cmd){
        AbstractProcessor processor = null;
        switch(cmd){
            case LOGIN :
                processor = (AbstractProcessor) SpringContextHolder.getApplicationContext().getBean(LoginProcessor.class);
                break;
            case HEART_BEAT:
                processor = (AbstractProcessor) SpringContextHolder.getApplicationContext().getBean(HeartBeatProcessor.class);
                break;
            case PRIVATE_MESSAGE:
                processor = (AbstractProcessor) SpringContextHolder.getApplicationContext().getBean(PrivateMessageProcessor.class);
                break;
            case GROUP_MESSAGE:
                processor = (AbstractProcessor) SpringContextHolder.getApplicationContext().getBean(GroupMessageProcessor.class);
                break;
            default:
                break;
        }
        return processor;
    }
}
