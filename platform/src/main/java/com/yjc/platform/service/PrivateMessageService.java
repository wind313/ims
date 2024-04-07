package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.vo.PrivateMessageVO;

public interface PrivateMessageService extends IService<PrivateMessage> {
    Long send(PrivateMessageVO privateMessageVO);

    void recall(Long id);

    void pullUnreadMessage();
}
