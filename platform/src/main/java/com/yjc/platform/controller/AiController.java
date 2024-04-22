package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.AiService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ai")
@Tag(name = "AI")
public class AiController {
    @Autowired
    private AiService aiService;

    private static final Long AiId = 1L;

    // 构造函数

    @GetMapping("/send")
    @Operation(summary = "发送消息",description = "返回AI消息")
    public Result<String> send(@RequestParam("message") String message){
        return ResultUtil.success(aiService.sendAndResponse(AiId,message));
    }

    @GetMapping("/history")
    @Operation(summary = "消息记录",description = "查看消息记录")
    public Result history(@NotNull(message = "页码不能为空") @RequestParam("page") Long page,
                          @NotNull(message = "size不能为空") @RequestParam("size") Long size){
        Long userId = SessionContext.getSession().getId();
        return ResultUtil.success(aiService.history(userId,AiId,page,size));

    }
}