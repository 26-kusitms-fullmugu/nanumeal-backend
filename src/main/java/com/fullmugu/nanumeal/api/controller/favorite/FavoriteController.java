package com.fullmugu.nanumeal.api.controller.favorite;

import com.fullmugu.nanumeal.api.dto.favorite.FavoriteDTO;
import com.fullmugu.nanumeal.api.dto.favorite.FavoriteRequestDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.favorite.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@Log4j2
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("")
    public ResponseEntity<List<FavoriteDTO>> getAllFavorites(@AuthenticationPrincipal User user){
        List<FavoriteDTO> favoriteDTOList = favoriteService.getList(user);

        return ResponseEntity.ok(favoriteDTOList);
    }

    @PostMapping("")
    public ResponseEntity register(@RequestBody FavoriteRequestDTO favoriteRequestDTO){
        favoriteService.register(favoriteRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity delete(@PathVariable("favoriteId") Long favoriteId, @AuthenticationPrincipal User user){
        favoriteService.delete(favoriteId, user);
        return ResponseEntity.noContent().build();
    }
}
