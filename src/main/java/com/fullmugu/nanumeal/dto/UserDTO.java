package com.fullmugu.nanumeal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
