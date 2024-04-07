package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.FriendVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/friend")
@Tag(name = "好友")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PutMapping("/add")
    @Operation(summary = "添加好友",description = "建立好友关系")
    public Result add(@NotNull(message = "好友id不能为空") @RequestParam("friendId") Long friendId){
        friendService.add(friendId);
        return ResultUtil.success();
    }

    @GetMapping("/list")
    @Operation(summary = "好友列表",description = "获取好友列表")
    public Result<List<FriendVO>> list(){

        return ResultUtil.success(friendService.friends());
    }

    @DeleteMapping("/delete/{friendId}")
    @Operation(summary = "删除好友",description = "删除好友关系")
    public Result delete(@NotNull(message = "好友id不能为空") @PathVariable("friendId") Long friendId){
        friendService.delete(friendId);
        return ResultUtil.success();
    }

    @PutMapping("/update")
    public Result update(@Valid @RequestBody FriendVO friendVO){
        return ResultUtil.success();
    }

}
