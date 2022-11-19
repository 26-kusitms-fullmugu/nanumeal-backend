package com.fullmugu.nanumeal.api.service.favorite;

import com.fullmugu.nanumeal.api.dto.favorite.FavoriteDTO;
import com.fullmugu.nanumeal.api.dto.favorite.FavoriteRequestDTO;
import com.fullmugu.nanumeal.api.entity.favorite.Favorite;
import com.fullmugu.nanumeal.api.entity.user.User;

import java.util.List;

public interface FavoriteService {

    List<FavoriteDTO> getList(User user);

    void register(FavoriteRequestDTO favoriteRequestDTO);

    void delete(Long favoriteId, User user);

    default FavoriteDTO entityToDTO(Favorite favorite){
        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .location(favorite.getRestaurantId().getLocation())
                .restaurantName(favorite.getRestaurantId().getName())
                .restaurantId(favorite.getRestaurantId().getId())
                .build();
        return favoriteDTO;
    }


}
