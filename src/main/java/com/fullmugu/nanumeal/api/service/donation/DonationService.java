package com.fullmugu.nanumeal.api.service.donation;

import com.fullmugu.nanumeal.api.dto.donation.DonationDTO;
import com.fullmugu.nanumeal.api.dto.donation.MakeDonationRequestDto;
import com.fullmugu.nanumeal.api.entity.donation.Donation;
import com.fullmugu.nanumeal.api.entity.user.User;

import java.util.List;

public interface DonationService {

    List<DonationDTO> myDonation(User user);

    Donation makeDonation(MakeDonationRequestDto makeDonationRequestDto, User user);

    default DonationDTO entityToDTO(Donation donation) {
        DonationDTO donationDTO = DonationDTO.builder()
                .donateDate(donation.getRegDate())
                .donateName(donation.getDonateUserId().getName())
                .donatePrice(donation.getDonPrice())
                .restaurantName(donation.getRestaurantId().getName())
                .build();
        return donationDTO;

    }
}
