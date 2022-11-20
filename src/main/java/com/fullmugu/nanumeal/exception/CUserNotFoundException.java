package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class CUserNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public CUserNotFoundException(String msg){
        super(msg);
    }

    public CUserNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
