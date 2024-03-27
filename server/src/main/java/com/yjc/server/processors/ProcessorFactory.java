package com.yjc.server.processors;

import com.yjc.common.enums.CommandType;
import com.yjc.server.utils.SpringContextHolder;

public class ProcessorFactory {
    public static FatherProcessor createProcessor(CommandType cmd){
        FatherProcessor processor = null;
        switch(cmd){
            case LOGIN :
                processor = (FatherProcessor) SpringContextHolder.getApplicationContext().getBean(LoginProcessor.class);
                break;
            case HEART_BEAT:
                processor = (FatherProcessor) SpringContextHolder.getApplicationContext().getBean(HeartBeatProcessor.class);
                break;
            case PRIVATE_MESSAGE:
                processor = (FatherProcessor) SpringContextHolder.getApplicationContext().getBean(PrivateMessageProcessor.class);
                break;
            case GROUP_MESSAGE:
                processor = (FatherProcessor) SpringContextHolder.getApplicationContext().getBean(GroupMessageProcessor.class);
                break;
            default:
                break;
        }
        return processor;
    }
}
