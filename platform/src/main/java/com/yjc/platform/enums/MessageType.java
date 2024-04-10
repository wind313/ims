package com.yjc.platform.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageType {
    TEXT(0,"文字"),
    FILE(1,"文件"),
    IMAGE(2,"图片"),
    VIDEO(3,"视频"),
    TIP(10,"系统提示"),

    RTC_CALL(101,"呼叫"),
    RTC_ACCEPT(102,"接受"),
    RTC_REJECT(103,"拒绝"),
    RTC_CANCEL(104,"取消"),
    RTC_FAILED(105,"失败"),
    RTC_HANGUP(106,"挂断"),
    RTC_CANDIDATE(107,"同步candidate");
    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
