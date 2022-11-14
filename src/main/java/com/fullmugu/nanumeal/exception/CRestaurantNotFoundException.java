package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;

public class CRestaurantNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public CRestaurantNotFoundException(String msg){
        super(msg);
    }

    public CRestaurantNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
