package com.fullmugu.nanumeal.api.entity.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Query("select r" +
            " from Restaurant r " +
            " WHERE r.remainDon > 0 " +
            " ORDER BY r.remainDon ASC")
    List<Restaurant> findAllOrderByRemainDon();

    @Query("select r " +
            "from Restaurant r WHERE r.remainDon < " +
            "(select MIN(m.price) from Menu m WHERE m.restaurantId = r)")
    List<Restaurant> findAllByMenuPrice();

//    @Query(value = "SELECT *," +
//            "            ( 6371 * acos( cos( radians( :latitude ) ) * cos( radians(x) ) * cos( radians(y) - radians( :longitude ) ) + sin( radians( :latitude ) ) * sin( radians(x) ) ) ) AS distance" +
//            "            FROM `restaurant`" +
//            "            ORDER BY distance ASC;", nativeQuery = true)
    @Query(value = "SELECT *," +
            "            ( 6371 * acos( cos( radians(37.573021918377506) ) * cos( radians(x) ) * cos( radians(y) - radians(126.98741790292372) ) + sin( radians(37.573021918377506) ) * sin( radians(x) ) ) ) AS distance" +
            "            FROM `restaurant`" +
            "            ORDER BY distance ASC;", nativeQuery = true)
    List<Restaurant> findAllByDistance(@Param("latitude")Double x, @Param("longitude")Double y);



    Optional<Restaurant> findByNameAndLocation(String name, String location);
}
