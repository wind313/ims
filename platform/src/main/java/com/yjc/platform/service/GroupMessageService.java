package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.common.model.GroupMessageInfo;
import com.yjc.platform.pojo.GroupMessage;
import com.yjc.platform.vo.GroupMessageVO;

import java.util.List;

public interface GroupMessageService extends IService<GroupMessage> {
    Long send(GroupMessageVO groupMessageVO);

    void recall(Long id);

    void pullUnreadMessage();

    List<GroupMessageInfo> history(Long groupId, Long page, Long size);
}
