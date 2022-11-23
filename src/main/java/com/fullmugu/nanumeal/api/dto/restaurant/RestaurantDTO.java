package com.fullmugu.nanumeal.api.dto.restaurant;

import com.fullmugu.nanumeal.api.dto.menu.MenuDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    private String name;

    private String location;

    private Long remain_don;

    private boolean GoB;

    private String information;

    private Double x;

    private Double y;

//    즐겨찾기 여부
    private boolean like;

    private List<MenuDTO> menuDTOList;
}
