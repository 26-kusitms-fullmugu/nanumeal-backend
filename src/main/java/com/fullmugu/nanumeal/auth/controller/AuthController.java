package com.fullmugu.nanumeal.auth.controller;

import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.s3.S3UploadService;
import com.fullmugu.nanumeal.auth.dto.FormLoginRequestDto;
import com.fullmugu.nanumeal.auth.dto.FormSignupRequestDto;
import com.fullmugu.nanumeal.auth.dto.KakaoSignupRequestDto;
import com.fullmugu.nanumeal.auth.jwt.JwtProperties;
import com.fullmugu.nanumeal.auth.service.AuthService;
import com.fullmugu.nanumeal.auth.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final S3UploadService s3UploadService;
    private final AuthService authService;

    @GetMapping("/token")
    public ResponseEntity<String> getLogin(@RequestParam("code") String code) { //(1)

        // 넘어온 인가 코드를 통해 access_token 발급
        OAuthToken oAuthToken = authService.getAccessToken(code);

        //(2)
        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        String jwtToken = authService.saveUserAndGetToken(oAuthToken.getAccess_token());

        //(3)
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        //(4)
        return ResponseEntity.ok().headers(headers).body("success");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> formSignUp(@RequestBody FormSignupRequestDto formSignupRequestDto) {

        String jwtToken = authService.saveUserAndGetToken(formSignupRequestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<String> kakaoSignUp(@RequestBody KakaoSignupRequestDto kakaoSignupRequestDto) {

        String jwtToken = authService.saveUserAndGetToken(kakaoSignupRequestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> formLogin(@RequestBody FormLoginRequestDto formLoginRequestDto) {

        User user = authService.findUserByFormLoginRequestDto(formLoginRequestDto);

        String jwtToken = authService.createToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @PostMapping("/email")
    public ResponseEntity<String> mailAuthorization(@RequestBody String email) throws Exception {
        return ResponseEntity.ok().body(authService.sendSimpleMessage(email));
    }

    @PostMapping("/verify/nickname")
    public ResponseEntity<String> verifyNickName(@RequestBody String nickName) {
        return ResponseEntity.ok().body(authService.checkNickNameDuplication(nickName));
    }

    @PostMapping("/verify/login-id")
    public ResponseEntity<String> verifyLoginId(@RequestBody String loginId) {
        return ResponseEntity.ok().body(authService.checkLoginIdDuplication(loginId));
    }

    @PostMapping("/verify/email")
    public ResponseEntity<String> verifyEmail(@RequestBody String email) {
        return ResponseEntity.ok().body(authService.checkEmailDuplication(email));
    }

    @PostMapping("/document")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(
                s3UploadService.upload(multipartFile)
        );
    }
}
