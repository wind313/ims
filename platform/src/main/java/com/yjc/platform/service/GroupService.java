package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.vo.GroupVO;

public interface GroupService extends IService<Group> {
    GroupVO create(String name);

    void update(GroupVO groupVO);
}
