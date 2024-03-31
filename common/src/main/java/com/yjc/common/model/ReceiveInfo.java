package com.yjc.common.model;

import lombok.Data;

import java.util.List;

@Data
public class ReceiveInfo<T> {
    private Integer cmd;
    private List<Long> receiveIds;
    private T data;
}
