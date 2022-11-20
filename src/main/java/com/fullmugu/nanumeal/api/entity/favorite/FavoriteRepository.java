package com.fullmugu.nanumeal.api.entity.favorite;

import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    @Query("select count (f.id) > 0" +
            " from Favorite f" +
            " where f.userId = :userId" +
            " and f.restaurantId = :restaurantId")
    boolean exists(@Param("userId") User user, @Param("restaurantId")Restaurant restaurant);

    @Query("select f " +
            "from Favorite f" +
            " where f.userId = :userId")
    List<Favorite> findAllByUserId(@Param("userId")User user);
}
