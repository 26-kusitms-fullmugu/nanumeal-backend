package com.fullmugu.nanumeal.api.service.restaurant;

import com.fullmugu.nanumeal.api.dto.menu.MenuDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.DistanceXYDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantListDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.XYDTO;
import com.fullmugu.nanumeal.api.entity.favorite.Favorite;
import com.fullmugu.nanumeal.api.entity.favorite.FavoriteRepository;
import com.fullmugu.nanumeal.api.entity.menu.Menu;
import com.fullmugu.nanumeal.api.entity.menu.MenuRepository;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.restaurant.RestaurantRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.exception.CRestaurantNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RestaurantServiceImpl implements RestaurantService{

    private final RestaurantRepository restaurantRepository;
    private final FavoriteRepository favoriteRepository;

    private final MenuRepository menuRepository;

    @Override
    public RestaurantDTO getOne(Long id, User user) {

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()->
                new CRestaurantNotFoundException("식당을 찾을 수 없습니다.", ErrorCode.NOT_FOUND));

        List<Menu> menuList = menuRepository.findByRestaurantId(restaurant);

        List<MenuDTO> menuDTOList = menuList.stream().map(menu -> MenuToDTO(menu)).collect(Collectors.toList());

        boolean favorite = favoriteRepository.exists(user, restaurant);

        RestaurantDTO restaurantDTO = entityToDTO(restaurant, favorite, menuDTOList);

        return restaurantDTO;
    }

    @Override
    public List<RestaurantListDTO> getListOrderByDonate(User user) {
        List<Restaurant> restaurantList = restaurantRepository.findAllOrderByRemainDon();
        List<RestaurantListDTO> restaurantListDTOList = restaurantList.stream().map(
                restaurant -> restaurantListToDTO(restaurant, favoriteRepository.exists(user, restaurant))).collect(Collectors.toList());

        return restaurantListDTOList;
    }

    @Override
    public List<RestaurantListDTO> getList(XYDTO xydto, User user) {

        List<Restaurant> restaurantList = restaurantRepository.findAllByXY(xydto.getSwx(), xydto.getNex(), xydto.getSwy(), xydto.getNey());
        List<RestaurantListDTO> restaurantDTOList = restaurantList.stream().map(restaurant ->
                // 리스트를 하나씩 빼서 현재 유저가 즐겨찾기 해놓은 곳인지도 출력
                restaurantListToDTO(restaurant, favoriteRepository.exists(user, restaurant))).collect(Collectors.toList());
        return restaurantDTOList;
    }

    @Override
    public List<RestaurantListDTO> getListByMenuPrice(User user) {
        List<Restaurant> restaurantList = restaurantRepository.findAllByMenuPrice();
        List<RestaurantListDTO> restaurantListDTOList = restaurantList.stream().map(restaurant ->
                restaurantListToDTO(restaurant, favoriteRepository.exists(user, restaurant))).collect(Collectors.toList());
        return restaurantListDTOList;
    }

    @Override
    public List<RestaurantListDTO> getListByDistance(User user, DistanceXYDTO distanceXYDTO) {
        log.info("res:::"+distanceXYDTO.getX(), distanceXYDTO.getY());
        List<Restaurant> restaurantList = restaurantRepository.findAllByDistance(distanceXYDTO.getX(), distanceXYDTO.getY());
        List<RestaurantListDTO> restaurantListDTOList = restaurantList.stream().map(restaurant ->
                restaurantListToDTO(restaurant, favoriteRepository.exists(user, restaurant))).collect(Collectors.toList());
        return restaurantListDTOList;
    }
}
