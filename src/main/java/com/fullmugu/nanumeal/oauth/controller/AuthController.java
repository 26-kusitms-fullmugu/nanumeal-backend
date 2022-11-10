package com.fullmugu.nanumeal.oauth.controller;

import com.fullmugu.nanumeal.oauth.jwt.JwtProperties;
import com.fullmugu.nanumeal.oauth.service.AuthService;
import com.fullmugu.nanumeal.oauth.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/oauth/token")
    public ResponseEntity getLogin(@RequestParam("code") String code) { //(1)

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
}
