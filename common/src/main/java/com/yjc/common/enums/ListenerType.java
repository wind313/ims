package com.yjc.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ListenerType {
    ALL(0,"全部消息"),
    PRIVATE(1,"私聊消息"),
    GROUP(2,"群聊消息");

    private Integer code;
    private String desc;
}
