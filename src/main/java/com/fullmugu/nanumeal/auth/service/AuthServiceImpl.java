package com.fullmugu.nanumeal.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullmugu.nanumeal.api.entity.user.Role;
import com.fullmugu.nanumeal.api.entity.user.Type;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.auth.dto.FormLoginRequestDto;
import com.fullmugu.nanumeal.auth.dto.FormSignupRequestDto;
import com.fullmugu.nanumeal.auth.dto.KakaoProfileDto;
import com.fullmugu.nanumeal.auth.jwt.JwtProperties;
import com.fullmugu.nanumeal.auth.token.OAuthToken;
import com.fullmugu.nanumeal.exception.*;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    @Value("${jwt.secret}")
    private String SECRET;

    private String ePw;

    public String saveUserAndGetToken(String token, Type type) {
        KakaoProfileDto profile = findProfile(token);

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if (user == null) {
            user = User.oauth2Register()
                    .kakaoId(profile.getId())
                    .name(profile.getKakao_account().getProfile().getNickname())
                    .password("Kakao" + profile.getId())
                    .type(type)
                    .email(profile.getKakao_account().getEmail())
                    .role(Role.ROLE_USER)
                    .provider("Kakao")
                    .build();

            userRepository.save(user);
            user.generatePassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.saveAndFlush(user);
        }

        return createToken(user); //(2)
    }

    @Override
    public String saveUserAndGetToken(FormSignupRequestDto formSignupRequestDto) {

        if (userRepository.findByLoginId(formSignupRequestDto.getLoginId()).isPresent()) {
            throw new CDuplicateLoginIdException("이미 존재하는 ID입니다.", ErrorCode.BAD_REQUEST);
        }

        if (userRepository.findByEmail(formSignupRequestDto.getEmail()) != null) {
            throw new CDuplicateEmailException("이미 존재하는 이메일입니다.", ErrorCode.BAD_REQUEST);
        }

        User user = User.formSignup()
                .loginId(formSignupRequestDto.getLoginId())
                .password(passwordEncoder.encode(formSignupRequestDto.getPassword()))
                .email(formSignupRequestDto.getEmail())
                .type(formSignupRequestDto.getType())
                .name(formSignupRequestDto.getName())
                .nickName(formSignupRequestDto.getNickName())
                .age(formSignupRequestDto.getAge())
                .location(formSignupRequestDto.getLocation())
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
            throw new CUserNotFoundException("ID 혹은 비밀번호가 일치하지 않습니다.", ErrorCode.FORBIDDEN);
        } else if (!passwordEncoder.matches(formLoginRequestDto.getPassword(), user.get().getPassword())) {
            log.info("PW does not matches.");
            throw new CUserNotFoundException("ID 혹은 비밀번호가 일치하지 않습니다.", ErrorCode.FORBIDDEN);
        } else if (user.get().getProvider() != null) {
            log.info("Social login user.");
            throw new CUserNotFoundException("소셜 로그인 유저입니다.", ErrorCode.FORBIDDEN);
        } else {
            return user.get();
        }
    }

    @Override
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

    @Override
    public String createToken(User user) {

        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("nickname", user.getName()) // claim은 User pk와 카카오 이름만 넣었음
                .sign(Algorithm.HMAC512(SECRET));
    }

    @Override
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

        if (accessTokenResponse.getStatusCodeValue() == 400) {
            throw new CAuthorizationCodeInvalidException("인가 코드가 유효하지 않습니다.", ErrorCode.BAD_REQUEST);
        }

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

    @Override
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상: " + to);
        log.info("인증 번호: " + ePw);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);// 보내는 대상
        message.setSubject("Nanumeal 회원가입 이메일 인증");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요.</h1>";
        msgg += "<h1> 서스펜디드 밀의 시작, 나누밀입니다.</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>항상 당신의 꿈을 응원합니다. 감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("tqdlfkg@naver.com", "Nanumeal_Admin"));// 보내는 사람

        return message;
    }

    @Override
    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {
        checkEmailDuplication(to);

        ePw = createKey(); // 랜덤 인증번호 생성

        MimeMessage message = createMessage(to); // 메일 발송
        try {// 예외처리
            javaMailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }


        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }

    @Override
    public String checkNickNameDuplication(String nickName) {
        if (userRepository.findByNickName(nickName).isPresent()) {
            throw new CDuplicateNicknameException("이미 존재하는 닉네임입니다.", ErrorCode.FORBIDDEN);
        } else {
            return "success";
        }
    }

    @Override
    public String checkLoginIdDuplication(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent()) {
            throw new CDuplicateLoginIdException("이미 존재하는 ID입니다.", ErrorCode.FORBIDDEN);
        } else {
            return "success";
        }
    }

    @Override
    public String checkEmailDuplication(String email) {
        if (userRepository.findByEmail(email) != null) {
            throw new CDuplicateEmailException("이미 존재하는 이메일입니다.", ErrorCode.FORBIDDEN);
        } else {
            return "success";
        }
    }
}

