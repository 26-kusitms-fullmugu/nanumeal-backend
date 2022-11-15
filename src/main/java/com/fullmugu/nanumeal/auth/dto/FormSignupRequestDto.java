package com.fullmugu.nanumeal.auth.dto;

import lombok.Data;

@Data
public class FormSignupRequestDto {

    private String loginId;

    private String password;

    private String email;
}
