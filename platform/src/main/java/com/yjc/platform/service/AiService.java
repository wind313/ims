package com.yjc.platform.service;

import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.platform.pojo.PrivateMessage;

import java.util.List;

public interface AiService {

    String sendAndResponse(Long AiId, String message);

    PrivateMessage send(Long userId,Long AiId, String message);

    PrivateMessage response(Long userId,Long AiId, String message);

    List<PrivateMessageInfo> history(Long userId,Long AiId, Long page, Long size);
}
