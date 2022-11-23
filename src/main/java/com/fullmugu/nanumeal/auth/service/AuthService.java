package com.fullmugu.nanumeal.auth.service;

import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.auth.dto.FormLoginRequestDto;
import com.fullmugu.nanumeal.auth.dto.FormSignupRequestDto;
import com.fullmugu.nanumeal.auth.dto.KakaoProfileDto;
import com.fullmugu.nanumeal.auth.dto.KakaoSignupRequestDto;
import com.fullmugu.nanumeal.auth.token.OAuthToken;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public interface AuthService {

    String saveUserAndGetToken(String token);

    String saveUserAndGetToken(FormSignupRequestDto formSignupRequestDto);

    String saveUserAndGetToken(KakaoSignupRequestDto kakaoSignupRequestDto);

    User findUserByFormLoginRequestDto(FormLoginRequestDto formLoginRequestDto);

    KakaoProfileDto findProfile(String token);

    String createToken(User user);

    OAuthToken getAccessToken(String code);

    MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;

    String createKey();

    String sendSimpleMessage(String to) throws Exception;

    String checkNickNameDuplication(String nickName);

    String checkLoginIdDuplication(String loginId);

    String checkEmailDuplication(String email);
}

