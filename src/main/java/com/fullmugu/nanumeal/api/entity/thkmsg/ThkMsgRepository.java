package com.fullmugu.nanumeal.api.entity.thkmsg;

import com.fullmugu.nanumeal.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThkMsgRepository extends JpaRepository<ThkMsg, Long> {

    @Query("select m" +
            " from ThkMsg m LEFT JOIN Donation d " +
            " ON d.restaurantId = m.resId WHERE d.donateUserId = :donateId")
    List<ThkMsg> findByDonateIdAndRestId(@Param("donateId") User donateId);

}
