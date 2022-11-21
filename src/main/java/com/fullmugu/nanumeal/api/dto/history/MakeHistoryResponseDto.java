package com.fullmugu.nanumeal.api.dto.history;

import com.fullmugu.nanumeal.api.entity.history.History;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MakeHistoryResponseDto {

    private Long usePrice;

    private LocalDateTime regDate;

    @Builder
    public MakeHistoryResponseDto(Long usePrice, LocalDateTime regDate) {
        this.usePrice = usePrice;
        this.regDate = regDate;
    }

    public static MakeHistoryResponseDto from(History history) {
        return MakeHistoryResponseDto
                .builder()
                .usePrice(history.getUsePrice())
                .regDate(history.getRegDate())
                .build();
    }
}
