package com.fullmugu.nanumeal.api.service;

import com.fullmugu.nanumeal.api.dto.UserDTO;
import com.fullmugu.nanumeal.api.entity.user.User;

public interface UserService {

//    유저정보 조회
    UserDTO getUser(Long id);

    default UserDTO entityToDto(User user){
        UserDTO userDTO = UserDTO.builder()
                .age(user.getAge())
                .name(user.getName())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .type(user.getType())
                .location(user.getLocation())
                .build();
        return userDTO;
    }

}
