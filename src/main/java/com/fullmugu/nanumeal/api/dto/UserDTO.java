package com.fullmugu.nanumeal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private List<String> roleSet = new ArrayList<>();

    private String email;

    private String nickName;

    private String name;

    private String passWord;

    private String age;

    private String location;

}
