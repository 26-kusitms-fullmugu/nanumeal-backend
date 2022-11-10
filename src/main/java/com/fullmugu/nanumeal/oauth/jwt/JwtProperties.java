package com.fullmugu.nanumeal.oauth.jwt;

public interface JwtProperties {
    String SECRET = "{}";
    int EXPIRATION_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
