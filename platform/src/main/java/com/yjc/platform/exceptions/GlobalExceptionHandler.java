package com.yjc.platform.exceptions;

import com.yjc.platform.enums.ResultCode;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;
            log.error("code:" + ex.getCode() + "    message:" + ex.getMessage());
            return ResultUtil.error(ex.getCode(), ex.getMessage());
        }
        else if(e instanceof UndeclaredThrowableException){
            GlobalException ex = (GlobalException)e.getCause();
            log.error("code:" + ex.getCode() + "    message:" + ex.getMessage());
            return ResultUtil.error(ex.getCode(), ex.getMessage());
        }
        else {
            log.error("message:" + e.getMessage());
            return ResultUtil.error(ResultCode.PROGRAM_ERROR);
        }
    }
}
