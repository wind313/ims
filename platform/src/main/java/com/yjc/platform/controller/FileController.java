package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.FileService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.ImageVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public Result<String> file(@Valid @RequestParam("file") MultipartFile file){
        return ResultUtil.success(fileService.uploadFile(file));
    }
    @PostMapping("/image")
    public Result<ImageVO> image(@Valid @RequestParam("file") MultipartFile file){
        return ResultUtil.success(fileService.uploadImage(file));
    }
}
