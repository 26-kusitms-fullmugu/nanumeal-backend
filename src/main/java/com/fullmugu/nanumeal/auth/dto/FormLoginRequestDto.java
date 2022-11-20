package com.fullmugu.nanumeal.auth.dto;

import lombok.Data;

@Data
public class FormLoginRequestDto {

    private String loginId;

    private String password;
}
