package com.fullmugu.nanumeal.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class KakaoController {

    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<String> getAuthorizationCode(@RequestParam("code") String authorizationCode) {
        log.info(authorizationCode);
        return ResponseEntity.ok(authorizationCode);
    }
}
