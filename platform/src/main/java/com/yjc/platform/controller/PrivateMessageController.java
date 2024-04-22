package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.PrivateMessageService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.PrivateMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/message/private")
@Tag(name = "私聊消息")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送私聊消息",description = "返回消息id")
    public Result<Long> send(@Valid @RequestBody PrivateMessageVO privateMessageVO){

        return ResultUtil.success(privateMessageService.send(privateMessageVO));
    }

    @DeleteMapping("/recall/{id}")
    @Operation(summary = "撤回私聊消息",description = "撤回私聊消息")
    public Result recall(@NotNull(message = "消息id不能为空") @PathVariable("id") Long id){
        privateMessageService.recall(id);
        return ResultUtil.success();
    }

    @PostMapping("/pullUnreadMessage")
    @Operation(summary = "拉取未读私聊消息",description = "拉取未读私聊消息")
    public Result pullUnreadMessage(){
        privateMessageService.pullUnreadMessage();
        return ResultUtil.success();
    }

    @GetMapping("/history")
    @Operation(summary = "消息记录",description = "查看消息记录")
    public Result history(@NotNull(message = "好友id不为空") @RequestParam("friendId") Long friendId,
                          @NotNull(message = "页码不能为空") @RequestParam("page") Long page,
                          @NotNull(message = "size不能为空") @RequestParam("size") Long size){
        Long userId = SessionContext.getSession().getId();
        return ResultUtil.success(privateMessageService.history(userId,friendId,page,size));

    }

}
