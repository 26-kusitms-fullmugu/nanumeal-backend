package com.fullmugu.nanumeal.api.entity.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    //위도 경도 계산해서 여러개 불러오기

    @Query("select r" +
            " from Restaurant r" +
            " WHERE (r.x BETWEEN :swx and :nex) AND" +
            " (r.y BETWEEN :swy and :ney)")
    List<Restaurant> findAllByXY(@Param("swx") Double swx, @Param("nex") Double nex,
                                 @Param("swy") Double swy, @Param("ney") Double ney);

    Optional<Restaurant> findByNameAndLocation(String name, String location);
}
