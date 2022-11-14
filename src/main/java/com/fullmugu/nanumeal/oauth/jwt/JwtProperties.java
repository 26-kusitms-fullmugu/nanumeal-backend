package com.fullmugu.nanumeal.oauth.jwt;

import org.springframework.stereotype.Component;

@Component
public interface JwtProperties {
    int EXPIRATION_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
