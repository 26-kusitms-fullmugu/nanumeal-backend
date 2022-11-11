package com.fullmugu.nanumeal.api.dto;

import com.fullmugu.nanumeal.api.entity.user.Role;
import com.fullmugu.nanumeal.api.entity.user.Type;
import lombok.Data;

@Data
public class UserInfoResponseDto {

    private String email;

    private String password;

    private String name;

    private Long age;

    private String location;

    private String username;

    private Type type;

    private Role role;

    private String provider;
}
