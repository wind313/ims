package com.yjc.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SendCode {
    SUCCESS(0,"发送成功"),
    NOT_ONLINE(1,"对方不在线"),
    NOT_FIND_CHANNEL(2,"未找到对方channel"),
    UNKNOWN_ERROR(3,"未知错误");

    private int code;
    private String decs;
}
