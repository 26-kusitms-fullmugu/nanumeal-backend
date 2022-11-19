package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;

public class CHistoryNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public CHistoryNotFoundException(String msg){
        super(msg);
    }

    public CHistoryNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
