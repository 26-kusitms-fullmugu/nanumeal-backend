package com.fullmugu.nanumeal.api.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 기부내역 1개
public class DonationDTO {

    // 기부자 이름
    private String donateName;

    // 식당 이름
    private String restaurantName;

    // 기부 날짜
    private Timestamp donateDate;

    // 기부 금액
    private Long donatePrice;

}
