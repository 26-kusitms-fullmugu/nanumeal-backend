package com.fullmugu.nanumeal.oauth.exception;

/* 토큰 검증 실패 Exception? */
public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super("Failed to generate Token.");
    }

    private TokenValidFailedException(String message) {
        super(message);
    }
}
