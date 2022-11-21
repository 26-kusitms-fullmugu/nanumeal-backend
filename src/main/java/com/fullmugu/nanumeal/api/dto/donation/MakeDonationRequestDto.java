package com.fullmugu.nanumeal.api.dto.donation;

import lombok.Data;

@Data
public class MakeDonationRequestDto {

    private String name;

    private String location;

    private Long money;
}
