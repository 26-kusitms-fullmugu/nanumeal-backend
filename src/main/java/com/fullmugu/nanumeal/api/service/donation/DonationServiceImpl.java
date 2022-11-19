package com.fullmugu.nanumeal.api.service.donation;

import com.fullmugu.nanumeal.api.dto.donation.DonationDTO;
import com.fullmugu.nanumeal.api.entity.donation.Donation;
import com.fullmugu.nanumeal.api.entity.donation.DonationRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class DonationServiceImpl implements DonationService{

    private final DonationRepository donationRepository;

    @Override
    public List<DonationDTO> myDonation(User user) {

        List<Donation> donations = donationRepository.findAllByUser_UserId(user);

        List<DonationDTO> donationsList = donations.stream().map(donation -> entityToDTO(donation)).collect(Collectors.toList());

        return donationsList;
    }
}
