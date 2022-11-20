package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;

public class CThkMsgNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public CThkMsgNotFoundException(String msg){
        super(msg);
    }

    public CThkMsgNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
