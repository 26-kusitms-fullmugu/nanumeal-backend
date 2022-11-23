package com.fullmugu.nanumeal.api.controller.restaurant;

import com.fullmugu.nanumeal.api.dto.restaurant.DistanceXYDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantListDTO;
import com.fullmugu.nanumeal.api.dto.restaurant.XYDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.restaurant.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
@Log4j2
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDTO> getOne(@PathVariable("restaurantId") Long id, @AuthenticationPrincipal User user){
        RestaurantDTO restaurantDTO = restaurantService.getOne(id, user);

        return ResponseEntity.ok(restaurantDTO);
    }

    // 위도 경도 계산해서 여러개 불러오기
    @GetMapping("/list")
    public ResponseEntity<List<RestaurantListDTO>> getList(@RequestBody XYDTO xydto, @AuthenticationPrincipal User user){
        List<RestaurantListDTO> restaurantDTOList = restaurantService.getList(xydto, user);
        return ResponseEntity.ok(restaurantDTOList);
    }

    //후원 금액순으로 불러오기
    @GetMapping("/list/donate")
    public ResponseEntity<List<RestaurantListDTO>> getListOrderByDonate(@AuthenticationPrincipal User user){
        List<RestaurantListDTO> restaurantListDTOList = restaurantService.getListOrderByDonate(user);
        return ResponseEntity.ok(restaurantListDTOList);
    }

    //후원 금액이 메뉴의 최소 금액보다 낮은 애들을 불러오기, 남은 금액 순
    @GetMapping("/list/remainDon")
    public ResponseEntity<List<RestaurantListDTO>> getListByMenuPrice(@AuthenticationPrincipal User user){
        List<RestaurantListDTO> restaurantListDTOList = restaurantService.getListByMenuPrice(user);
        return ResponseEntity.ok(restaurantListDTOList);
    }

    //거리순으로 뽑기
    @GetMapping("/list/distance")
    public ResponseEntity<List<RestaurantListDTO>> getListByDistance(@AuthenticationPrincipal User user, @RequestBody DistanceXYDTO distanceXYDTO){
        log.info("con:::"+ distanceXYDTO);
        List<RestaurantListDTO> restaurantListDTOList = restaurantService.getListByDistance(user, distanceXYDTO);
        return ResponseEntity.ok(restaurantListDTOList);
    }

}
