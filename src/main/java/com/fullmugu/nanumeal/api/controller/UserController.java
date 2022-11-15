package com.fullmugu.nanumeal.api.controller;

import com.fullmugu.nanumeal.api.dto.UserInfoResponseDto;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(@AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(UserInfoResponseDto.from(user));
    }
}
