package com.yjc.platform.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200,"成功"),
    NOT_LOGIN(400,"未登录"),
    INVALID_TOKEN(401,"token已失效"),
    PROGRAM_ERROR(500,"系统繁忙，请稍后再试");

    private Integer code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
