package com.fullmugu.nanumeal.exception;

import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class CInvalidHistoryMoneyException extends RuntimeException {

    private ErrorCode errorCode;

    public CInvalidHistoryMoneyException(String msg) {
        super(msg);
    }

    public CInvalidHistoryMoneyException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
