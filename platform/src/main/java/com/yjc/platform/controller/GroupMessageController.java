package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.GroupMessageService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.GroupMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message/group")
@Tag(name = "群聊消息")
public class GroupMessageController {

    @Autowired
    private GroupMessageService groupMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送群聊消息",description = "返回消息id")
    public Result<Long> send(@Valid @RequestBody GroupMessageVO groupMessageVO){
        return ResultUtil.success(groupMessageService.send(groupMessageVO));
    }

    @DeleteMapping("/recall/{id}")
    @Operation(summary = "撤回群聊消息",description = "撤回群聊消息")
    public Result recall(@NotNull(message = "消息id不能为空") @PathVariable("id") Long id){
        groupMessageService.recall(id);
        return ResultUtil.success();
    }

    @PostMapping("/pullUnreadMessage")
    @Operation(summary = "拉取未读消息",description = "拉取未读消息")
    public Result pullUnreadMessage(){
        groupMessageService.pullUnreadMessage();
        return ResultUtil.success();
    }

    @GetMapping("/history")
    @Operation(summary = "消息记录",description = "查看消息记录")
    public Result history(@NotNull(message = "群聊id不为空") @RequestParam("groupId") Long groupId,
                          @NotNull(message = "页码不能为空") @RequestParam("page") Long page,
                          @NotNull(message = "size不能为空") @RequestParam("size") Long size){
        return ResultUtil.success(groupMessageService.history(groupId,page,size));

    }


}
