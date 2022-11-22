package com.fullmugu.nanumeal.api.entity.menu;

import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select m " +
            "from Menu m WHERE m.restaurantId = :restId")
    List<Menu> findByRestaurantId(@Param("restId")Restaurant restId);
}
