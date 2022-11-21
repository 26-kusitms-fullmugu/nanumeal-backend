package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class CDuplicateNicknameException extends RuntimeException {
    private ErrorCode errorCode;

    public CDuplicateNicknameException(String msg) {
        super(msg);
    }

    public CDuplicateNicknameException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
