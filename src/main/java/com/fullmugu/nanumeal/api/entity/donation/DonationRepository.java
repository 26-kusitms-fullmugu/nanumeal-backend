package com.fullmugu.nanumeal.api.entity.donation;

import com.fullmugu.nanumeal.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {


    @Query(value = "select d" +
            " from Donation d where d.donateUserId = :donateUser")
    List<Donation> findAllByUser_UserId(@Param("donateUser") User user);


}
