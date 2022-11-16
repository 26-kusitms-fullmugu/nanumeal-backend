package com.fullmugu.nanumeal.auth.dto;

import com.fullmugu.nanumeal.api.entity.user.Type;
import lombok.Data;

@Data
public class FormSignupRequestDto {

    private String loginId;

    private String password;

    private String email;

    private String name;

    private String nickName;

    private Long age;

    private Type type;

    private String location;
}
