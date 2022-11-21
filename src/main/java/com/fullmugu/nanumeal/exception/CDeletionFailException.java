package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;

public class CDeletionFailException extends RuntimeException {
    private ErrorCode errorCode;

    public CDeletionFailException(String msg) {
        super(msg);
    }

    public CDeletionFailException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
