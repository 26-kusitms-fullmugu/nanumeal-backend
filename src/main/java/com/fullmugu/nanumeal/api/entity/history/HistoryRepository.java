package com.fullmugu.nanumeal.api.entity.history;

import com.fullmugu.nanumeal.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("select h" +
            " from History h WHERE h.userId = :userId")
    List<History> findAllByUserId(@Param("userId") User user);

}
