package com.fullmugu.nanumeal.api.service.donation;

import com.fullmugu.nanumeal.api.dto.donation.DonationDTO;
import com.fullmugu.nanumeal.api.dto.donation.MakeDonationRequestDto;
import com.fullmugu.nanumeal.api.entity.donation.Donation;
import com.fullmugu.nanumeal.api.entity.donation.DonationRepository;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.restaurant.RestaurantRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.exception.CRestaurantNotFoundException;
import com.fullmugu.nanumeal.exception.CUserNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class DonationServiceImpl implements DonationService {

    private final RestaurantRepository restaurantRepository;

    private final DonationRepository donationRepository;

    @Override
    public List<DonationDTO> myDonation(User user) {

        List<Donation> donations = donationRepository.findAllByUser_UserId(user);

        List<DonationDTO> donationsList = donations.stream().map(donation -> entityToDTO(donation)).collect(Collectors.toList());

        return donationsList;
    }

    @Transactional
    @Override
    public Donation makeDonation(MakeDonationRequestDto makeDonationRequestDto, User user) {
        if (user == null) {
            throw new CUserNotFoundException("유효하지 않은 사용자입니다.", ErrorCode.FORBIDDEN);
        }

        Restaurant restaurant = restaurantRepository
                .findByNameAndLocation(makeDonationRequestDto.getName(), makeDonationRequestDto.getLocation())
                .orElseThrow(() -> new CRestaurantNotFoundException("식당 이름이나 위치가 올바르지 않습니다.", ErrorCode.BAD_REQUEST));

        restaurant.setRemainDon(restaurant.getRemainDon() + makeDonationRequestDto.getMoney());
        restaurantRepository.save(restaurant);

        return donationRepository.save(
                Donation.builder()
                        .donateUserId(user)
                        .restaurantId(restaurant)
                        .donPrice(makeDonationRequestDto.getMoney())
//                        .regDate(LocalDateTime.now())
                        .isThanked(false)
                        .build()
        );
    }
}
