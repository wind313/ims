package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.GroupMessage;
import com.yjc.platform.vo.GroupMessageVO;

public interface GroupMessageService extends IService<GroupMessage> {
    Long send(GroupMessageVO groupMessageVO);

    void recall(Long id);

    void pullUnreadMessage();
}
