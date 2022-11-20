package com.fullmugu.nanumeal.api.dto.user;

import lombok.Data;

@Data
public class InputUserInfoRequestDto {

    private String name;

    private String nickName;

    private Long age;

    private String location;
}
