package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.vo.PrivateMessageVO;

import java.util.List;

public interface PrivateMessageService extends IService<PrivateMessage> {
    Long send(PrivateMessageVO privateMessageVO);

    void recall(Long id);

    void pullUnreadMessage();

    List<PrivateMessageInfo> history(Long friendId, Long page, Long size);
}
