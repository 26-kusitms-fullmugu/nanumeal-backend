package com.fullmugu.nanumeal.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    private String message;

    private String code;

    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getErrorCode();
    }

    public ErrorResponse(ErrorCode errorCode, String message){
        this.status = errorCode.getStatus();
        this.code = errorCode.getErrorCode();
        this.message = message;
    }
}
