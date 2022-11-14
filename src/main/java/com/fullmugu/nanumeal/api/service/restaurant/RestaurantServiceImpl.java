package com.fullmugu.nanumeal.api.service.restaurant;

import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantDTO;
import com.fullmugu.nanumeal.api.entity.favorite.Favorite;
import com.fullmugu.nanumeal.api.entity.favorite.FavoriteRepository;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.restaurant.RestaurantRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.exception.CRestaurantNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RestaurantServiceImpl implements RestaurantService{

    private final RestaurantRepository restaurantRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public RestaurantDTO getOne(Long id, User user) {

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()->
                new CRestaurantNotFoundException("식당을 찾을 수 없습니다.", ErrorCode.NOT_FOUND));

        boolean favorite = favoriteRepository.exists(user, restaurant);

        RestaurantDTO restaurantDTO = entityToDTO(restaurant, favorite);

        return restaurantDTO;
    }
}
