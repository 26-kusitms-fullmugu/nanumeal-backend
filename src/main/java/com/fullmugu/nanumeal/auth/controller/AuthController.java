package com.fullmugu.nanumeal.auth.controller;

import com.fullmugu.nanumeal.auth.dto.FormSignupRequestDto;
import com.fullmugu.nanumeal.auth.jwt.JwtProperties;
import com.fullmugu.nanumeal.auth.service.AuthService;
import com.fullmugu.nanumeal.auth.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

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

        if (jwtToken.equals("Duplicated ID.")) {
            return ResponseEntity.ok().body("Duplicated ID.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }
}
