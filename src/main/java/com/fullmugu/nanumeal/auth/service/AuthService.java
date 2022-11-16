package com.fullmugu.nanumeal.auth.service;

import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.auth.dto.FormLoginRequestDto;
import com.fullmugu.nanumeal.auth.dto.FormSignupRequestDto;
import com.fullmugu.nanumeal.auth.dto.KakaoProfileDto;
import com.fullmugu.nanumeal.auth.token.OAuthToken;

public interface AuthService {

    String saveUserAndGetToken(String token);

    String saveUserAndGetToken(FormSignupRequestDto formSignupRequestDto);

    User findUserByFormLoginRequestDto(FormLoginRequestDto formLoginRequestDto);

    KakaoProfileDto findProfile(String token);

    String createToken(User user);

    OAuthToken getAccessToken(String code);
}

