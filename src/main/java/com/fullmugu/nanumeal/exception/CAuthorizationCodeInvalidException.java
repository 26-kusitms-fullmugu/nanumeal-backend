package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;

public class CAuthorizationCodeInvalidException extends RuntimeException {
    private ErrorCode errorCode;

    public CAuthorizationCodeInvalidException(String msg) {
        super(msg);
    }

    public CAuthorizationCodeInvalidException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
