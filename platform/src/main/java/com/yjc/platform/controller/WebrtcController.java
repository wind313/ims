package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.WebrtcService;
import com.yjc.platform.util.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "webrtc视频单人通话")
@RestController
@RequestMapping("/webrtc/private")
public class WebrtcController {

    @Autowired
    private WebrtcService webrtcService;

    @Operation(summary = "呼叫")
    @PostMapping("/call")
    public Result call(@RequestParam("uid") Long uid, @RequestBody String offer){
        webrtcService.call(uid,offer);
        return ResultUtil.success();
    }

    @Operation(summary = "接受")
    @PostMapping("/accept")
    public Result accept(@RequestParam("uid") Long uid,@RequestBody String answer){
        webrtcService.accept(uid,answer);
        return ResultUtil.success();
    }

    @Operation(summary = "拒绝")
    @PostMapping("/reject")
    public Result reject(@RequestParam("uid") Long uid){
        webrtcService.reject(uid);
        return ResultUtil.success();
    }

    @Operation(summary = "取消")
    @PostMapping("/cancel")
    public Result cancel(@RequestParam("uid") Long uid){
        webrtcService.cancel(uid);
        return ResultUtil.success();
    }

    @Operation(summary = "失败")
    @PostMapping("/failed")
    public Result failed(@RequestParam("uid") Long uid,@RequestBody String reason){
        webrtcService.failed(uid,reason);
        return ResultUtil.success();
    }

    @Operation(summary = "挂断")
    @PostMapping("/handup")
    public Result handup(@RequestParam("uid") Long uid){
        webrtcService.handup(uid);
        return ResultUtil.success();
    }


    @Operation(summary = "同步candidate")
    @PostMapping("/candidate")
    public Result candidate(@RequestParam("uid") Long uid,@RequestBody String candidate){
        webrtcService.candidate(uid,candidate);
        return ResultUtil.success();
    }

    @Operation(summary = "获取iceservers")
    @GetMapping("/iceservers")
    public Result iceservers(){
        return ResultUtil.success(webrtcService.iceservers());
    }


}
