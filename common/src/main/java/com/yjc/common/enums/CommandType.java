package com.yjc.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommandType {
    LOGIN(0,"登录"),
    HEART_BEAT(1,"心跳"),
    LOGOUT(2,"下线"),
    PRIVATE_MESSAGE(3,"私聊消息"),
    GROUP_MESSAGE(4,"群聊消息");

    public Integer getCode() {
        return code;
    }


    private  Integer code;
    private String desc;
    public static CommandType fromCode(Integer code){
        for(CommandType commandType:values()){
            if(commandType.code.equals(code)){
                return commandType;
            }
        }
        return null;
    }
}
