package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;

public class CNotAllowedDeleteFavoriteException extends RuntimeException{

    private ErrorCode errorCode;

    public CNotAllowedDeleteFavoriteException(String msg){
        super(msg);
    }

    public CNotAllowedDeleteFavoriteException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
