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
import com.fullmugu.nanumeal.auth.dto.KakaoSignupRequestDto;
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
import java.util.Objects;
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

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    private String ePw;

    public String saveUserAndGetToken(String token) {
        KakaoProfileDto profile = findProfile(token);

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if (user == null) {
            user = User.oauth2Register()
                    .kakaoId(profile.getId())
                    .name(profile.getKakao_account().getProfile().getNickname())
                    .password("Kakao" + profile.getId())
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
    public String saveUserAndGetToken(KakaoSignupRequestDto kakaoSignupRequestDto) {

        Optional<User> getUser = userRepository.findByKakaoIdAndEmail(kakaoSignupRequestDto.getKakaoId(), kakaoSignupRequestDto.getEmail());

        User verifyUser = userRepository.findByEmail(kakaoSignupRequestDto.getEmail());

        if (getUser.isPresent() && verifyUser != null) {
            if (!Objects.equals(getUser.get().getKakaoId(), verifyUser.getKakaoId())) {
                throw new CDuplicateEmailException("이미 가입된 이메일입니다. 아이디 및 비밀번호로 로그인해주세요.", ErrorCode.BAD_REQUEST);
            }
            return createToken(getUser.get());
        } else {
            User user = User
                    .oauth2Register()
                    .role(Role.ROLE_USER)
                    .kakaoId(kakaoSignupRequestDto.getKakaoId())
                    .name(kakaoSignupRequestDto.getName())
                    .email(kakaoSignupRequestDto.getEmail())
                    .provider("Kakao")
                    .password("Kakao" + kakaoSignupRequestDto.getKakaoId())
                    .build();

            userRepository.save(user);
            user.generatePassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.saveAndFlush(user);
            return createToken(user); //(2)
        }

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
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", CLIENT_SECRET); // 생략 가능!

        //(5)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = null;
        //(6)
        try {
            accessTokenResponse = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
        } catch (RuntimeException e) {
            throw new CAuthorizationCodeInvalidException("인가 코드가 유효하지 않습니다.", ErrorCode.BAD_REQUEST);
        }


        if (accessTokenResponse.getStatusCodeValue() != 200) {
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

        String msgg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "    <!-- Compiled with Bootstrap Email version: 1.1.2 -->\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "    <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" +
                "    <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <meta name=\"format-detection\" content=\"telephone=no, date=no, address=no, email=no\">\n" +
                "    <style type=\"text/css\">\n" +
                "    body, table, td {\n" +
                "        font-family:Helvetica, Arial, sans-serif !important\n" +
                "    }\n" +
                "\n" +
                "    .ExternalClass {\n" +
                "        width:100%\n" +
                "    }\n" +
                "\n" +
                "    .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {\n" +
                "        line-height:150%\n" +
                "    }\n" +
                "\n" +
                "    a {\n" +
                "        text-decoration:none\n" +
                "    }\n" +
                "\n" +
                "    * {\n" +
                "        color:inherit\n" +
                "    }\n" +
                "\n" +
                "    a[x-apple-data-detectors], u + #body a, #MessageViewBody a {\n" +
                "        color: inherit;\n" +
                "        text-decoration: none;\n" +
                "        font-size: inherit;\n" +
                "        font-family: inherit;\n" +
                "        font-weight: inherit;\n" +
                "        line-height:inherit\n" +
                "    }\n" +
                "\n" +
                "    img {\n" +
                "        -ms-interpolation-mode:bicubic\n" +
                "    }\n" +
                "\n" +
                "    table:not([class^=s-]) {\n" +
                "        font-family: Helvetica, Arial, sans-serif;\n" +
                "        mso-table-lspace: 0pt;\n" +
                "        mso-table-rspace: 0pt;\n" +
                "        border-spacing: 0px;\n" +
                "        border-collapse:collapse\n" +
                "    }\n" +
                "\n" +
                "    table:not([class^=s-]) td {\n" +
                "        border-spacing: 0px;\n" +
                "        border-collapse:collapse\n" +
                "    }\n" +
                "\n" +
                "    @media screen and (max-width: 600px) {\n" +
                "        .w-full, .w-full > tbody > tr > td {\n" +
                "            width:100% !important\n" +
                "        }\n" +
                "\n" +
                "        .w-24, .w-24 > tbody > tr > td {\n" +
                "            width:96px !important\n" +
                "        }\n" +
                "\n" +
                "        .w-40, .w-40 > tbody > tr > td {\n" +
                "            width:160px !important\n" +
                "        }\n" +
                "\n" +
                "        .p-lg-10:not(table), .p-lg-10:not(.btn) > tbody > tr > td, .p-lg-10.btn td a {\n" +
                "            padding:0 !important\n" +
                "        }\n" +
                "\n" +
                "        .p-3:not(table), .p-3:not(.btn) > tbody > tr > td, .p-3.btn td a {\n" +
                "            padding:12px !important\n" +
                "        }\n" +
                "\n" +
                "        .p-6:not(table), .p-6:not(.btn) > tbody > tr > td, .p-6.btn td a {\n" +
                "            padding:24px !important\n" +
                "        }\n" +
                "\n" +
                "        * [class * =s-lg-] > tbody > tr > td {\n" +
                "            font-size: 0 !important;\n" +
                "            line-height: 0 !important;\n" +
                "            height:0 !important\n" +
                "        }\n" +
                "\n" +
                "        .s-4 > tbody > tr > td {\n" +
                "            font-size: 16px !important;\n" +
                "            line-height: 16px !important;\n" +
                "            height:16px !important\n" +
                "        }\n" +
                "\n" +
                "        .s-6 > tbody > tr > td {\n" +
                "            font-size: 24px !important;\n" +
                "            line-height: 24px !important;\n" +
                "            height:24px !important\n" +
                "        }\n" +
                "\n" +
                "        .s-10 > tbody > tr > td {\n" +
                "            font-size: 40px !important;\n" +
                "            line-height: 40px !important;\n" +
                "            height: 40px !important\n" +
                "        }\n" +
                "    }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"bg-light\" style=\"outline: 0; width: 100%; min-width: 100%; height: 100%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; font-family: Helvetica, Arial, sans-serif; line-height: 24px; font-weight: normal; font-size: 16px; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; box-sizing: border-box; color: #000000; margin: 0; padding: 0; border-width: 0;\" bgcolor=\"#f7fafc\">\n" +
                "    <table class=\"bg-light body\" valign=\"top\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"outline: 0; width: 100%; min-width: 100%; height: 100%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; font-family: Helvetica, Arial, sans-serif; line-height: 24px; font-weight: normal; font-size: 16px; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; box-sizing: border-box; color: #000000; margin: 0; padding: 0; border-width: 0;\" bgcolor=\"#f7fafc\">\n" +
                "        <tbody>\n" +
                "            <tr>\n" +
                "                <td valign=\"top\" style=\"line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\" bgcolor=\"#f7fafc\">\n" +
                "                    <table class=\"container\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\">\n" +
                "                        <tbody>\n" +
                "                            <tr>\n" +
                "                                <td align=\"center\" style=\"line-height: 24px; font-size: 16px; margin: 0; padding: 0 16px;\">\n" +
                "                                    <!--[if (gte mso 9)|(IE)]>\n" +
                "                                                          <table align=\"center\" role=\"presentation\">\n" +
                "                                                            <tbody>\n" +
                "                                                              <tr>\n" +
                "                                                                <td width=\"600\">\n" +
                "                                                        <![endif]-->\n" +
                "                                    <table align=\"center\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%; max-width: 600px; margin: 0 auto;\">\n" +
                "                                        <tbody>\n" +
                "                                            <tr>\n" +
                "                                                <td style=\"line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\">\n" +
                "                                                    <table class=\"s-10 w-full\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\" width=\"100%\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 40px; font-size: 40px; width: 100%; height: 40px; margin: 0;\" align=\"left\" width=\"100%\" height=\"40\">\n" +
                "                                                                                                    &#160;\n" +
                "                                                                                                  </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <table class=\"ax-center\" role=\"presentation\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: 0 auto;\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\">\n" +
                "                                                                    <img class=\"w-24\" src=\"https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F65286235-271f-4957-a338-137dacf5b3b4%2F%25E1%2584%2582%25E1%2585%25A1%25E1%2584%2582%25E1%2585%25AE%25E1%2584%2586%25E1%2585%25B5%25E1%2586%25AF%25E1%2584%2585%25E1%2585%25A9%25E1%2584%2580%25E1%2585%25A94x_1.png?table=block&id=c751f91b-ce6e-466b-9178-5ccbd589131c&spaceId=a15e736d-1e85-48b0-a7fc-c3d9de9b90dd&width=2000&userId=f6e8f054-82ca-4158-9df7-e2428c505fe2&cache=v2\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; display: block; width: 96px; border-style: none; border-width: 0;\" width=\"96\">\n" +
                "                                                                </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <table class=\"s-10 w-full\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\" width=\"100%\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 40px; font-size: 40px; width: 100%; height: 40px; margin: 0;\" align=\"left\" width=\"100%\" height=\"40\">\n" +
                "                                                                                                    &#160;\n" +
                "                                                                                                  </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <table class=\"card p-6 p-lg-10 space-y-4\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-radius: 6px; border-collapse: separate !important; width: 100%; overflow: hidden; border: 1px solid #e2e8f0;\" bgcolor=\"#ffffff\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 24px; font-size: 16px; width: 100%; margin: 0; padding: 40px;\" align=\"left\" bgcolor=\"#ffffff\">\n" +
                "                                                                    <h1 class=\"h3 fw-700\" style=\"padding-top: 0; padding-bottom: 0; font-weight: 700 !important; vertical-align: baseline; font-size: 28px; line-height: 33.6px; margin: 0; color:#DA3915;\" align=\"center\">\n" +
                "                                                                                                          나누밀\n" +
                "                                                                                                        </h1>\n" +
                "                                                                    <table class=\"s-4 w-full\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\" width=\"100%\">\n" +
                "                                                                        <tbody>\n" +
                "                                                                            <tr>\n" +
                "                                                                                <td style=\"line-height: 16px; font-size: 16px; width: 100%; height: 16px; margin: 0;\" align=\"left\" width=\"100%\" height=\"16\">\n" +
                "                                                                                                                            &#160;\n" +
                "                                                                                                                          </td>\n" +
                "                                                                            </tr>\n" +
                "                                                                        </tbody>\n" +
                "                                                                    </table>\n" +
                "                                                                    <p class=\"\" style=\"line-height: 24px; font-size: 16px; width: 100%; margin: 0;\" align=\"center\">\n" +
                "\n" +
                "                                                                                                              안녕하세요. 서스펜디드 밀의 시작, 나누밀입니다.\n" +
                "                                                                        <br>\n" +
                "                                                                        <br>\n" +
                "\n" +
                "                                                                                                              회원가입을 진심으로 환영합니다!\n" +
                "                                                                        <br>\n" +
                "                                                                        <br>\n" +
                "\n" +
                "                                                                                                              아래의 인증코드 8자리를 입력하여 회원가입을 완료해주세요.\n<br>" +
                "\n<h2 align=\"center\">" + ePw + "</h2>" +
                "                                                                                                            \n" +
                "                                                                    </p>\n" +
                "\n" +
                "                                                                    <table class=\"s-4 w-full\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\" width=\"100%\">\n" +
                "                                                                        <tbody>\n" +
                "                                                                            <tr>\n" +
                "                                                                                <td style=\"line-height: 16px; font-size: 16px; width: 100%; height: 16px; margin: 0;\" align=\"left\" width=\"100%\" height=\"16\">\n" +
                "                                                                                                                            &#160;\n" +
                "                                                                                                                          </td>\n" +
                "                                                                            </tr>\n" +
                "                                                                        </tbody>\n" +
                "                                                                    </table>\n" +
                "                                                                    <table class=\"btn btn-primary p-3 fw-700\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-radius: 6px; border-collapse: separate !important; font-weight: 700 !important;\">\n" +
                "                                                                        <tbody>\n" +
                "                                                                            <tr>\n" +
                "                                                                                <td style=\"line-height: 24px; font-size: 16px; border-radius: 6px; font-weight: 700 !important; margin: 0;\" align=\"center\" bgcolor=\"#0d6efd\">\n" +
                "                                                                                    <!-- <a href=\"https://app.bootstrapemail.com/templates\" style=\"color: #ffffff; font-size: 16px; font-family: Helvetica, Arial, sans-serif; text-decoration: none; border-radius: 6px; line-height: 20px; display: block; font-weight: 700 !important; white-space: nowrap; background-color: #0d6efd; padding: 12px; border: 1px solid #0d6efd;\">Visit Website</a> -->\n" +
                "                                                                                </td>\n" +
                "                                                                            </tr>\n" +
                "                                                                        </tbody>\n" +
                "                                                                    </table>\n" +
                "                                                                </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <table class=\"s-10 w-full\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\" width=\"100%\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 40px; font-size: 40px; width: 100%; height: 40px; margin: 0;\" align=\"left\" width=\"100%\" height=\"40\">\n" +
                "                                                                                                    &#160;\n" +
                "                                                                                                  </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <table class=\"ax-center\" role=\"presentation\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: 0 auto;\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\">\n" +
                "                                                                    <img class=\"w-40\" src=\"https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Ff8a635f5-ed1d-4cc6-bd38-4e95651c604b%2F%25E1%2584%258C%25E1%2585%25A1%25E1%2584%2589%25E1%2585%25A1%25E1%2586%25AB_64x_1.png?table=block&id=9bd7dce8-d06e-468c-9c21-00d1468e5167&spaceId=a15e736d-1e85-48b0-a7fc-c3d9de9b90dd&width=2000&userId=f6e8f054-82ca-4158-9df7-e2428c505fe2&cache=v2\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; display: block; width: 160px; border-style: none; border-width: 0;\" width=\"160\">\n" +
                "                                                                </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <table class=\"s-6 w-full\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\" width=\"100%\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 24px; font-size: 24px; width: 100%; height: 24px; margin: 0;\" align=\"left\" width=\"100%\" height=\"24\">\n" +
                "                                                                                                    &#160;\n" +
                "                                                                                                  </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <div class=\"text-muted text-center\" style=\"color: #718096;\" align=\"center\">\n" +
                "                                                        <h4>해당 이메일로 나누밀 가입을 시도한적이 없다면 고객센터로 문의 바랍니다.</h4>\n" +
                "                                                        <img class=\"w-40\" src=\"https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F38ec13e1-4637-4fb2-8e1d-d037aefe2d36%2FUntitled.png?table=block&id=8ee10eef-d33d-466f-99ea-f0b289e943e2&spaceId=a15e736d-1e85-48b0-a7fc-c3d9de9b90dd&width=2000&userId=f6e8f054-82ca-4158-9df7-e2428c505fe2&cache=v2\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; display: block; width: 240px; border-style: none; border-width: 0;\" width=\"240\">\n" +
                "\n" +
                "                                                    </div>\n" +
                "                                                    <table class=\"s-6 w-full\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\" width=\"100%\">\n" +
                "                                                        <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td style=\"line-height: 24px; font-size: 24px; width: 100%; height: 24px; margin: 0;\" align=\"left\" width=\"100%\" height=\"24\">\n" +
                "                                                                                                    &#160;\n" +
                "                                                                                                  </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </tbody>\n" +
                "                                    </table>\n" +
                "                                    <!--[if (gte mso 9)|(IE)]>\n" +
                "                                                        </td>\n" +
                "                                                      </tr>\n" +
                "                                                    </tbody>\n" +
                "                                                  </table>\n" +
                "                                                        <![endif]-->\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </tbody>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>\n";

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

