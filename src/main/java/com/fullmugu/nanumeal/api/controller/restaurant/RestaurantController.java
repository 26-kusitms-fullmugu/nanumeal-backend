package com.fullmugu.nanumeal.api.controller.restaurant;

import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantDTO;
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
    public ResponseEntity<List<RestaurantDTO>> getList(@RequestBody XYDTO xydto, @AuthenticationPrincipal User user){
        List<RestaurantDTO> restaurantDTOList = restaurantService.getList(xydto, user);
        return ResponseEntity.ok(restaurantDTOList);
    }



}
