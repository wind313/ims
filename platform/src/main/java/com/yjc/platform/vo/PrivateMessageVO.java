package com.yjc.platform.vo;

import lombok.Data;

@Data
public class PrivateMessageVO {
    private Long receiveId;
    private String content;
    private Integer type;
}