package com.fullmugu.nanumeal.api.controller.user;

import com.fullmugu.nanumeal.api.dto.user.InputUserInfoRequestDto;
import com.fullmugu.nanumeal.api.dto.user.InputUserTypeRequestDto;
import com.fullmugu.nanumeal.api.dto.user.UserInfoResponseDto;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(@AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(UserInfoResponseDto.from(user));
    }

    @PutMapping("/info")
    public ResponseEntity<UserInfoResponseDto> inputUserInfo(@AuthenticationPrincipal User user, @RequestBody InputUserInfoRequestDto inputUserInfoRequestDto) {
        return ResponseEntity.ok().body(UserInfoResponseDto.from(userService.setUserInfo(user, inputUserInfoRequestDto)));
    }

    @PostMapping("/type")
    public ResponseEntity<UserInfoResponseDto> inputUserType(@AuthenticationPrincipal User user, @RequestBody InputUserTypeRequestDto inputUserTypeRequestDto) {
        return ResponseEntity.ok().body(UserInfoResponseDto.from(userService.setUserType(user, inputUserTypeRequestDto)));
    }

    @DeleteMapping("/info")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userService.deleteUser(user));
    }
}
