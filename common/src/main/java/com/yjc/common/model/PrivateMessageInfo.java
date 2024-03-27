package com.yjc.common.model;

import lombok.Data;

import java.util.Date;

@Data
public class PrivateMessageInfo {
    private Long id;
    private Long sendId;
    private Long receiveId;
    private String content;
    private Integer type;
    private Date sendTime;
}
