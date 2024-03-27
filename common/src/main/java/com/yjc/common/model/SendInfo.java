package com.yjc.common.model;

import lombok.Data;

@Data
public class SendInfo<T> {
    private Integer cmd;
    private T data;
}
