package com.yjc.common.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yjc.common.serializer.DateToLongSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class GroupMessageInfo {
    private Long id;
    private Long sendId;
    private Long receiveId;
    private String content;
    private Integer type;
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date sendTime;
}
