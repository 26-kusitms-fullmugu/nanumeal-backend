package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class CDuplicateEmailException extends RuntimeException {
    private ErrorCode errorCode;

    public CDuplicateEmailException(String msg) {
        super(msg);
    }

    public CDuplicateEmailException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
