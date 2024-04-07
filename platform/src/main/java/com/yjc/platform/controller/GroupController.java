package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.GroupMemberVO;
import com.yjc.platform.vo.GroupVO;
import com.yjc.platform.vo.InviteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@Tag(name = "群聊")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    @Operation(summary = "创建群聊",description = "创建群聊")
    public Result<GroupVO> create(@NotBlank(message = "群名不能为空") @RequestParam("name") String name){
        return ResultUtil.success(groupService.create(name));
    }

    @PutMapping("/update")
    @Operation(summary = "修改群聊信息",description = "修改群聊信息")
    public Result update(@Valid @RequestBody GroupVO groupVO){
        groupService.update(groupVO);
        return ResultUtil.success();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除群聊",description = "删除群聊")
    public Result delete(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        groupService.delete(id);
        return ResultUtil.success();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "查询群聊",description = "查询群聊信息")
    public Result<GroupVO> findById(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        return ResultUtil.success(groupService.findById(id));
    }

    @GetMapping("/members/{id}")
    @Operation(summary = "查询群聊成员",description = "查询群聊成员")
    public Result<List<GroupMemberVO>> findMemberByGroupId(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        return ResultUtil.success(groupService.findMemberByGroupId(id));
    }

    @DeleteMapping("/quit/{id}")
    @Operation(summary = "退出群聊",description = "退出群聊")
    public Result quit(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        groupService.quit(id);
        return ResultUtil.success();
    }

    @DeleteMapping("/kick/{groupId}")
    @Operation(summary = "踢出群聊",description = "将成员踢出群聊")
    public Result kick(@NotNull(message = "群id不能为空") @PathVariable("groupId") Long groupId,@NotNull(message = "用户id不能为空") @RequestParam("userId") Long userId){
        groupService.kick(groupId,userId);
        return ResultUtil.success();
    }

    @GetMapping("/list")
    @Operation(summary = "群聊列表",description = "查询群聊列表")
    public Result<List<GroupVO>> list(){
        return ResultUtil.success(groupService.getList());
    }

    @PostMapping("/invite")
    @Operation(summary = "邀请进群",description = "邀请好友进群")
    public Result invite(@Valid @RequestBody InviteVO inviteVO){
        groupService.invite(inviteVO);
        return ResultUtil.success();
    }



}
