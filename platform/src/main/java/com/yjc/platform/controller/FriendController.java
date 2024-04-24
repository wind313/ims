package com.yjc.platform.controller;

import com.yjc.platform.constants.RedisKey;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.FriendVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/friend")
@Tag(name = "好友")
public class FriendController {

    @Autowired
    private FriendService friendService;


    @PutMapping("/add")
    @Operation(summary = "关注",description = "关注对方")
    public Result add(@NotNull(message = "对方id不能为空") @RequestParam("friendId") Long friendId){
        friendService.add(friendId);
        return ResultUtil.success();
    }

    @DeleteMapping("/delete/{friendId}")
    @Operation(summary = "取消关注",description = "取消关注")
    public Result delete(@NotNull(message = "对方id不能为空") @PathVariable("friendId") Long friendId){
        friendService.delete(friendId);
        return ResultUtil.success();
    }

    @PutMapping("/update")
    public Result update(@Valid @RequestBody FriendVO friendVO){
        return ResultUtil.success();
    }
    @Operation(summary = "查找关注用户",description = "查找关注用户信息")
    @GetMapping("/find/{friendId}")
    public Result find(@NotNull(message = "对方id不能为空") @PathVariable("friendId") Long friendId){
        return ResultUtil.success(friendService.findByFriendId(friendId));
    }

    @GetMapping("/concerns")
    @Operation(summary = "关注列表",description = "获取关注列表")
    public Result<List<FriendVO>> concerns(){
        return ResultUtil.success(friendService.concerns());
    }

    @GetMapping("/fans")
    @Operation(summary = "粉丝列表",description = "获取粉丝列表")
    public Result<List<FriendVO>> fans(){
        return ResultUtil.success(friendService.fans());
    }

    @GetMapping("/list")
    @Operation(summary = "互关列表",description = "获取互关列表")
    public Result<List<FriendVO>> list(){
        return ResultUtil.success(friendService.friends());
    }


}
