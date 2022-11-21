package com.fullmugu.nanumeal.api.dto.user;

import com.fullmugu.nanumeal.api.entity.user.Role;
import com.fullmugu.nanumeal.api.entity.user.Type;
import com.fullmugu.nanumeal.api.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserInfoResponseDto {

    private String email;

    private String name;

    private Long age;

    private String location;

    private Type type;

    private Role role;

    private String provider;

    @Builder
    public UserInfoResponseDto(String email, String name, Long age, String location, Type type, Role role, String provider) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.location = location;
        this.type = type;
        this.role = role;
        this.provider = provider;
    }

    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .age(user.getAge())
                .location(user.getLocation())
                .type(user.getType())
                .role(user.getRole())
                .provider(user.getProvider())
                .build();
    }
}
