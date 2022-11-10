package com.fullmugu.nanumeal.oauth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullmugu.nanumeal.api.entity.user.Role;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.oauth.dto.KakaoProfileDto;
import com.fullmugu.nanumeal.oauth.jwt.JwtProperties;
import com.fullmugu.nanumeal.oauth.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository; //(1)

    public String saveUserAndGetToken(String token) { //(1)
        KakaoProfileDto profile = findProfile(token);

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if (user == null) {
            user = User.builder()
                    .kakaoId(profile.getId())
                    .name(profile.getKakao_account().getProfile().getNickname())
                    .email(profile.getKakao_account().getEmail())
                    .role(Role.ROLE_USER).build();

            userRepository.save(user);
        }

        return createToken(user); //(2)
    }

    public KakaoProfileDto findProfile(String token) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfileDto profileDto = null;
        try {
            profileDto = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfileDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return profileDto;

    }

    public String createToken(User user) {

        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("nickname", user.getName()) // claim은 User pk와 카카오 이름만 넣었음
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public OAuthToken getAccessToken(String code) {

        //(2)
        RestTemplate rt = new RestTemplate();

        //(3)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //(4)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "d041b6f16d3bc4f0a3bc384015073302");
        params.add("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao");
        params.add("code", code);
        params.add("client_secret", "vZgr0RMvRVWSsMHG7WnZbs7jyZSuA9Zy"); // 생략 가능!

        //(5)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        //(6)
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //(7)
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(accessTokenResponse.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oAuthToken; //(8)
    }
}

