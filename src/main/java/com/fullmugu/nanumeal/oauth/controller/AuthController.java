package com.fullmugu.nanumeal.oauth.controller;

import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.oauth.service.AuthService;
import com.fullmugu.nanumeal.oauth.token.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/oauth/token")
    public OAuthToken getLogin(@RequestParam("code") String code) {

        OAuthToken oAuthToken = authService.getAccessToken(code);

        User user = authService.saveUser(oAuthToken.getAccess_token());

        return oAuthToken;
    }
}
