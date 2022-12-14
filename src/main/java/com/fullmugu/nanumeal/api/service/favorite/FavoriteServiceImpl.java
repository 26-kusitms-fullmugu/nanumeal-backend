package com.fullmugu.nanumeal.api.service.favorite;

import com.fullmugu.nanumeal.api.dto.favorite.FavoriteDTO;
import com.fullmugu.nanumeal.api.dto.favorite.FavoriteRequestDTO;
import com.fullmugu.nanumeal.api.entity.favorite.Favorite;
import com.fullmugu.nanumeal.api.entity.favorite.FavoriteRepository;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.restaurant.RestaurantRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.exception.CFavoriteNotFoundException;
import com.fullmugu.nanumeal.exception.CNotAllowedDeleteFavoriteException;
import com.fullmugu.nanumeal.exception.CRestaurantNotFoundException;
import com.fullmugu.nanumeal.exception.CUserNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;


    @Override
    public List<FavoriteDTO> getList(User user) {

        List<Favorite> favoriteList = favoriteRepository.findAllByUserId(user);

        List<FavoriteDTO> favoriteDTOList = favoriteList.stream().map(favorite -> entityToDTO(favorite)).collect(Collectors.toList());

        return favoriteDTOList;
    }

    @Override
    public void register(FavoriteRequestDTO favoriteRequestDTO) {
        Favorite favorite = DtoToEntity(favoriteRequestDTO);
        favoriteRepository.save(favorite);
    }

    @Override
    public void delete(Long favoriteId, User user) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new CFavoriteNotFoundException("??????????????? ?????? ???????????????.", ErrorCode.NOT_FOUND));
        Long favoriteUserId = favorite.getUserId().getId();
        Long userId = user.getId();

        // ??????????????? ??? ????????? ???????????? ?????? ???????????? ???????????? ?????? ??????
        if (!favoriteUserId.equals(userId)) {
            throw new CNotAllowedDeleteFavoriteException("??????????????? ????????? ????????? ????????????.", ErrorCode.UNAUTHORIZED);
        } else if (favoriteUserId.equals(userId)) {
            favoriteRepository.deleteById(favoriteId);
        }

    }

    Favorite DtoToEntity(FavoriteRequestDTO favoriteRequestDTO){

        Restaurant restaurant = restaurantRepository.findById(favoriteRequestDTO.getRestaurantId())
                .orElseThrow(() -> new CRestaurantNotFoundException("????????? ????????? ????????????.", ErrorCode.NOT_FOUND));
        User user = userRepository.findById(favoriteRequestDTO.getUserId())
                .orElseThrow(() -> new CUserNotFoundException("????????? ?????? ??? ????????????.", ErrorCode.NOT_FOUND));

        Favorite favorite = Favorite.builder()
                .restaurantId(restaurant)
                .userId(user)
                .build();
        return favorite;
    }
}
