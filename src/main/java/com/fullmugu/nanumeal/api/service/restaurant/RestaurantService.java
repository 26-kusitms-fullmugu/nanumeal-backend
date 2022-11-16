package com.fullmugu.nanumeal.api.service.restaurant;

import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.XYDTO;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.user.User;

import java.util.List;

public interface RestaurantService {

    RestaurantDTO getOne(Long id, User user);

    List<RestaurantDTO> getList(XYDTO xydto, User user);

    default RestaurantDTO entityToDTO(Restaurant restaurant, boolean like){
        RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                .GoB(restaurant.getGoB())
                .information(restaurant.getInformation())
                .location(restaurant.getLocation())
                .name(restaurant.getName())
                .x(restaurant.getX())
                .y(restaurant.getY())
                .like(like)
                .build();
        return restaurantDTO;
    }
}
