package com.fullmugu.nanumeal.api.controller.donation;

import com.fullmugu.nanumeal.api.dto.donation.MakeDonationRequestDto;
import com.fullmugu.nanumeal.api.dto.donation.MakeDonationResponseDto;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.donation.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/donation")
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/make")
    public ResponseEntity<MakeDonationResponseDto> makeDonation(@RequestBody MakeDonationRequestDto makeDonationRequestDto, @AuthenticationPrincipal User user) {

        MakeDonationResponseDto makeDonationResponseDto = MakeDonationResponseDto.from(donationService.makeDonation(makeDonationRequestDto, user));

        return ResponseEntity.ok(makeDonationResponseDto);
    }
}
