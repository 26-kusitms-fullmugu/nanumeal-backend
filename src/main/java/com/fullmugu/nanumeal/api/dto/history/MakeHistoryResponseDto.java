package com.fullmugu.nanumeal.api.dto.history;

import com.fullmugu.nanumeal.api.entity.history.History;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class MakeHistoryResponseDto {

    private Long usePrice;

    private Timestamp regDate;

    @Builder
    public MakeHistoryResponseDto(Long usePrice, Timestamp regDate) {
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
