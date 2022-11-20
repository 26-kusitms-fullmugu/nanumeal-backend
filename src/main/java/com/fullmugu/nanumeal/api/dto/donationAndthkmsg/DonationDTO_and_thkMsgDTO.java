package com.fullmugu.nanumeal.api.dto.donationAndthkmsg;

import com.fullmugu.nanumeal.api.dto.donation.DonationDTO;
import com.fullmugu.nanumeal.api.dto.thkmsg.ThkMsgDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationDTO_and_thkMsgDTO {

    private List<DonationDTO> donationDTOList;

    private List<ThkMsgDTO> thkMsgDTOList;
}
