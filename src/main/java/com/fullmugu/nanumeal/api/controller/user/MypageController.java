package com.fullmugu.nanumeal.api.controller.user;

import com.fullmugu.nanumeal.api.dto.user.UserDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Log4j2
public class MypageController {

    private final UserService userService;

//    회원정보 조회
    @GetMapping("")
    public ResponseEntity<UserDTO> getOne(@AuthenticationPrincipal User user){
        UserDTO userDTO = userService.getUser(user.getId());
        return ResponseEntity.ok().body(userDTO);
    }
}
