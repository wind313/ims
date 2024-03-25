package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.GroupMessageService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.GroupMessageVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message/group")
public class GroupMessageController {

    @Autowired
    private GroupMessageService groupMessageService;

    @PostMapping("/send")
    public Result<Long> send(@Valid @RequestBody GroupMessageVO groupMessageVO){
        return ResultUtil.success(groupMessageService.send(groupMessageVO));
    }

    @DeleteMapping("/recall/{id}")
    public Result recall(@NotNull(message = "消息id不能为空") @PathVariable("id") Long id){
        groupMessageService.recall(id);
        return ResultUtil.success();
    }
}
