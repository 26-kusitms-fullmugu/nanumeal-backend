package com.fullmugu.nanumeal.api.service.user;


import com.fullmugu.nanumeal.api.dto.user.InputUserInfoRequestDto;
import com.fullmugu.nanumeal.api.dto.user.InputUserTypeRequestDto;
import com.fullmugu.nanumeal.api.dto.user.UserDTO;
import com.fullmugu.nanumeal.api.entity.user.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    User getUserFromReq(HttpServletRequest request);

    User setUserInfo(User user, InputUserInfoRequestDto inputUserInfoRequestDto);

    User setUserType(User user, InputUserTypeRequestDto inputUserTypeRequestDto);

    String deleteUser(User user);

    User getUserById(Long id);

    UserDTO getUser(Long id);

    default UserDTO entityToDto(User user) {
        UserDTO userDTO = UserDTO.builder()
                .userId(user.getLoginId())
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
