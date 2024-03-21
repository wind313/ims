package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.GroupMessageService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.GroupMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/group")
public class GroupMessageController {

    @Autowired
    private GroupMessageService groupMessageService;

    @PostMapping("/send")
    public Result<Long> send(@RequestBody GroupMessageVO groupMessageVO){
        return ResultUtil.success(groupMessageService.send(groupMessageVO));
    }
}
