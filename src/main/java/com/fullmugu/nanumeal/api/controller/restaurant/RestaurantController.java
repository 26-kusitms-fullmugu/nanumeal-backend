package com.fullmugu.nanumeal.api.controller.restaurant;

import com.fullmugu.nanumeal.api.dto.restaurant.RestaurantDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.restaurant.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
