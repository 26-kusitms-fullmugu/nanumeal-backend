package com.fullmugu.nanumeal.api.controller.thkmsg;

import com.fullmugu.nanumeal.api.dto.donation.DonationDTO;
import com.fullmugu.nanumeal.api.dto.donationAndthkmsg.DonationDTO_and_thkMsgDTO;
import com.fullmugu.nanumeal.api.dto.thkmsg.ThanksMessageRequestDto;
import com.fullmugu.nanumeal.api.dto.thkmsg.ThanksMessageResponseDto;
import com.fullmugu.nanumeal.api.dto.thkmsg.ThkMsgDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.donation.DonationService;
import com.fullmugu.nanumeal.api.service.thkmsg.ThkMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/thanks")
@Log4j2
public class ThkMsgController {

    private final ThkMsgService thkMsgService;
    private final DonationService donationService;

    @GetMapping("/donation")
    public ResponseEntity<DonationDTO_and_thkMsgDTO> donationAndMessage(@AuthenticationPrincipal User user){
        List<ThkMsgDTO> thkMsgDTOList = thkMsgService.getAllMsg(user);
        List<DonationDTO> donationDTOList = donationService.myDonation(user);

        DonationDTO_and_thkMsgDTO donationDTO_and_thkMsgDTO = DonationDTO_and_thkMsgDTO.builder()
                .thkMsgDTOList(thkMsgDTOList)
                .donationDTOList(donationDTOList)
                .build();

        return ResponseEntity.ok(donationDTO_and_thkMsgDTO);

    }

    @PostMapping("/make")
    public ResponseEntity<ThanksMessageResponseDto> makeThankMessage(@RequestBody ThanksMessageRequestDto thanksMessageRequestDto, @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(ThanksMessageResponseDto.from(
                thkMsgService.makeThankMessage(thanksMessageRequestDto, user))
        );

    }
}
