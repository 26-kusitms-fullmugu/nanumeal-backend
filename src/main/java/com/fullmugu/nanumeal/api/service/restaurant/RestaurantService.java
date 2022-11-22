package com.fullmugu.nanumeal.api.service.restaurant;

import com.fullmugu.nanumeal.api.dto.menu.MenuDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantListDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.XYDTO;
import com.fullmugu.nanumeal.api.entity.menu.Menu;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.user.User;

import java.util.List;

public interface RestaurantService {

    RestaurantDTO getOne(Long id, User user);

    List<RestaurantListDTO> getList(XYDTO xydto, User user);

    default RestaurantDTO entityToDTO(Restaurant restaurant, boolean like, List<MenuDTO> menuDTOList){
        RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                .GoB(restaurant.getGoB())
                .information(restaurant.getInformation())
                .location(restaurant.getLocation())
                .name(restaurant.getName())
                .x(restaurant.getX())
                .y(restaurant.getY())
                .like(like)
                .menuDTOList(menuDTOList)
                .build();
        return restaurantDTO;
    }

    default RestaurantListDTO restaurantListToDTO(Restaurant restaurant, boolean like){

        RestaurantListDTO restaurantDTO = RestaurantListDTO.builder()
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

    default MenuDTO MenuToDTO(Menu menu){
        MenuDTO menuDTO = MenuDTO.builder()
                .menuName(menu.getName())
                .image(menu.getImage())
                .price(menu.getPrice())
                .restaurantName(menu.getRestaurantId().getName())
                .build();

        return menuDTO;
    }
}
