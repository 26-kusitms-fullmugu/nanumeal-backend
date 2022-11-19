package com.fullmugu.nanumeal.api.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    private String name;

    private String location;

    private boolean GoB;

    private String information;

    private Double x;

    private Double y;

//    즐겨찾기 여부
    private boolean like;
}
