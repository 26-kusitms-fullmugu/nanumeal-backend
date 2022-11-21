package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class CDuplicateLoginIdException extends RuntimeException {
    private ErrorCode errorCode;

    public CDuplicateLoginIdException(String msg) {
        super(msg);
    }

    public CDuplicateLoginIdException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
