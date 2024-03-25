package com.yjc.platform.controller;

import com.yjc.platform.pojo.Friend;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.FriendVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PutMapping("/add")
    public Result add(@NotNull(message = "好友id不能为空") @RequestParam("friendId") Long friendId){
        friendService.add(friendId);
        return ResultUtil.success();
    }

    @GetMapping("/list")
    public Result<List<FriendVO>> list(){

        return ResultUtil.success(friendService.friends());
    }

    @DeleteMapping("/delete/{friendId}")
    public Result delete(@NotNull(message = "好友id不能为空") @PathVariable("friendId") Long friendId){
        friendService.delete(friendId);
        return ResultUtil.success();
    }

}
