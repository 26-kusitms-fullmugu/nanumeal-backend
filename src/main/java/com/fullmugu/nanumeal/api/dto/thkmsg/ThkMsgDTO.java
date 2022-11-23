package com.fullmugu.nanumeal.api.dto.thkmsg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThkMsgDTO {

    //보내는 결식 아동
    private String childName;

    private String feeling;

    //먹은 식당 이름
    private String restaurantName;

    //메시지 이름
    private String content;

}
