package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class CInvalidJwtException extends RuntimeException {

    private ErrorCode errorCode;

    public CInvalidJwtException(String msg) {
        super(msg);
    }

    public CInvalidJwtException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
