package com.fullmugu.nanumeal.api.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {

    private String restaurantName;

    private String menuName;

    private Long price;

    private String image;
}
