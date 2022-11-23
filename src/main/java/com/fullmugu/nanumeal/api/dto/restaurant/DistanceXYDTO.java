package com.fullmugu.nanumeal.api.dto.restaurant;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceXYDTO {
    
    //위도, 경도
    private Double x;
    private Double y;
}
