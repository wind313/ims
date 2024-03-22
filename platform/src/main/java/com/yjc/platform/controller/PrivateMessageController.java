package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.PrivateMessageService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.PrivateMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message/private")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    @PostMapping("/send")
    public Result<Long> send(@RequestBody PrivateMessageVO privateMessageVO){

        return ResultUtil.success(privateMessageService.send(privateMessageVO));
    }

    @DeleteMapping("/recall/{id}")
    public Result recall(@PathVariable("id") Long id){
        privateMessageService.recall(id);
        return ResultUtil.success();
    }
}
