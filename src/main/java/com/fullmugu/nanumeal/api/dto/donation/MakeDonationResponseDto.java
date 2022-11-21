package com.fullmugu.nanumeal.api.dto.donation;

import com.fullmugu.nanumeal.api.entity.donation.Donation;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class MakeDonationResponseDto {

    private Long donPrice;

    private Timestamp regDate;

    private Boolean isThanked;

    @Builder
    public MakeDonationResponseDto(Long donPrice, Timestamp regDate, Boolean isThanked) {
        this.donPrice = donPrice;
        this.regDate = regDate;
        this.isThanked = isThanked;
    }

    public static MakeDonationResponseDto from(Donation donation) {
        return MakeDonationResponseDto.builder()
                .donPrice(donation.getDonPrice())
                .regDate(donation.getRegDate())
                .isThanked(donation.getIsThanked())
                .build();
    }
}
