package com.fullmugu.nanumeal.oauth.service;

import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.oauth.dto.KakaoProfileDto;
import com.fullmugu.nanumeal.oauth.token.OAuthToken;

public interface AuthService {

    String saveUserAndGetToken(String token);

    KakaoProfileDto findProfile(String token);

    String createToken(User user);

    OAuthToken getAccessToken(String code);
}

