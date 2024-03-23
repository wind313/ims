package com.yjc.platform.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileType {

    FILE(0,"文件"),
    IMAGE(1,"图片"),
    VOICE(2,"音频"),
    VIDEO(2,"视频");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
