package com.fullmugu.nanumeal.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullmugu.nanumeal.api.entity.user.Role;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.auth.dto.FormLoginRequestDto;
import com.fullmugu.nanumeal.auth.dto.FormSignupRequestDto;
import com.fullmugu.nanumeal.auth.dto.KakaoProfileDto;
import com.fullmugu.nanumeal.auth.jwt.JwtProperties;
import com.fullmugu.nanumeal.auth.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String SECRET;

    public String saveUserAndGetToken(String token) {
        KakaoProfileDto profile = findProfile(token);

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if (user == null) {
            user = User.oauth2Register()
                    .kakaoId(profile.getId())
                    .nickName(profile.getKakao_account().getProfile().getNickname())
                    .password("Kakao" + profile.getId())
                    .email(profile.getKakao_account().getEmail())
                    .role(Role.ROLE_USER)
                    .provider("Kakao")
                    .build();

            userRepository.save(user);
            user.generatePassword();
            userRepository.saveAndFlush(user);
        }

        return createToken(user); //(2)
    }

    @Override
    public String saveUserAndGetToken(FormSignupRequestDto formSignupRequestDto) {

        if (userRepository.findByLoginId(formSignupRequestDto.getLoginId()).isPresent()) {
            return "Duplicated ID.";
        }

        if (userRepository.findByEmail(formSignupRequestDto.getEmail()) != null) {
            return "Duplicated email.";
        }

        User user = User.formSignup()
                .loginId(formSignupRequestDto.getLoginId())
                .password(formSignupRequestDto.getPassword())
                .email(formSignupRequestDto.getEmail())
                .role(Role.ROLE_USER)
                .build();

        userRepository.saveAndFlush(user);

        return createToken(user); //(2)
    }

    @Override
    public User findUserByFormLoginRequestDto(FormLoginRequestDto formLoginRequestDto) {
        Optional<User> user = userRepository.findByLoginId(formLoginRequestDto.getLoginId());
        if (user.isEmpty()) {
            log.info("ID does not exists.");
            return null;
        } else if (!user.get().getPassword().equals(formLoginRequestDto.getPassword())) {
            log.info("PW does not matches.");
            return null;
        } else {
            return user.get();
        }
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
                .sign(Algorithm.HMAC512(SECRET));
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

