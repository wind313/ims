package com.yjc.platform.Exceptions;

import com.yjc.platform.enums.ResultCode;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException{

    private Integer code;
    private String message;

    public GlobalException(Integer code, String message) {
        this.code = code;
        this.message = message;

    }
    public GlobalException( String message) {
        this.code = ResultCode.PROGRAM_ERROR.getCode();
        this.message = message;

    }
    public GlobalException( ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();

    }



}
