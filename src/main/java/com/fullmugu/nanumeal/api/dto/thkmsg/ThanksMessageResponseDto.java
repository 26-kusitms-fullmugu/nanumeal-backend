package com.fullmugu.nanumeal.api.dto.thkmsg;

import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsg;
import lombok.Builder;
import lombok.Data;

@Data
public class ThanksMessageResponseDto {

    private String restaurantName;

    private String feeling;

    private String message;

    @Builder
    public ThanksMessageResponseDto(String restaurantName, String feeling, String message) {
        this.restaurantName = restaurantName;
        this.feeling = feeling;
        this.message = message;
    }


    public static ThanksMessageResponseDto from(ThkMsg thkMsg) {
        return ThanksMessageResponseDto
                .builder()
                .restaurantName(thkMsg.getResId().getName())
                .feeling(thkMsg.getFeeling())
                .message(thkMsg.getMessage())
                .build();
    }
}
