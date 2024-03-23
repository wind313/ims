package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public Result<GroupVO> create(@RequestParam("name") String name){
        return ResultUtil.success(groupService.create(name));
    }

    @PutMapping("/update")
    public Result update(@RequestBody GroupVO groupVO){
        groupService.update(groupVO);
        return ResultUtil.success();
    }
}
