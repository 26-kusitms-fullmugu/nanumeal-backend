package com.fullmugu.nanumeal.api.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XYDTO {

    // 남서쪽 좌표 x, y,  남서쪽이 더 낮음 
    private Double swx;
    private Double swy;

    // 북동쪽 좌표 x, y, 북동쪽이 더 높음
    private Double nex;
    private Double ney;
}
