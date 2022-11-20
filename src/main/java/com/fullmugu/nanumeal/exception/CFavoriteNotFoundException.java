package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;

public class CFavoriteNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public CFavoriteNotFoundException(String msg){
        super(msg);
    }

    public CFavoriteNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
