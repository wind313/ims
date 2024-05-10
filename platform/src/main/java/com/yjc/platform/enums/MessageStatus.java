package com.yjc.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum MessageStatus {

    UNREAD(0,"未读"),
    ALREADY_READ(1,"已读"),
    RECALL(2,"已撤回"),
    SEND_DELETE(3,"发送者已删除"),
    RECEIVE_DELETE(4,"接收者已删除");

    private Integer code;
    private String desc;

    public Integer code(){
        return code;
    }
}
