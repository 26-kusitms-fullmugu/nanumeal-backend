package com.fullmugu.nanumeal.auth.dto;

import lombok.Data;

@Data
public class KakaoSignupRequestDto {

    private Long kakaoId;

    private String name;

    private String email;
}
