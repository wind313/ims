package com.yjc.common.model;

import com.yjc.common.enums.SendCode;
import lombok.Data;

@Data
public class ResultInfo<T> {
    private Long receiveId;
    private SendCode code;
    private T messageInfo;
}
