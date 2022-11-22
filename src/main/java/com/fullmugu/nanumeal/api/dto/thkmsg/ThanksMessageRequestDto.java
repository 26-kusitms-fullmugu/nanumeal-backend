package com.fullmugu.nanumeal.api.dto.thkmsg;

import lombok.Data;

@Data
public class ThanksMessageRequestDto {

    private String restaurantName;

    private String restaurantLocation;

    private String feeling;

    private String message;

}
