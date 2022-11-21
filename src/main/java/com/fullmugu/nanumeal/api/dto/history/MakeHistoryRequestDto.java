package com.fullmugu.nanumeal.api.dto.history;

import lombok.Data;

@Data
public class MakeHistoryRequestDto {

    private String name;

    private String location;

    private Long money;

}
