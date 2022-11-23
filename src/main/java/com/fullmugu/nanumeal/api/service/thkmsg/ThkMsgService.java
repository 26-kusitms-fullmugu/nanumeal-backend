package com.fullmugu.nanumeal.api.service.thkmsg;

import com.fullmugu.nanumeal.api.dto.thkmsg.ThanksMessageRequestDto;
import com.fullmugu.nanumeal.api.dto.thkmsg.ThkMsgDTO;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsg;
import com.fullmugu.nanumeal.api.entity.user.User;

import java.util.List;

public interface ThkMsgService {

    //감사 메시지 리스트
    List<ThkMsgDTO> getAllMsg(User user);

    ThkMsg makeThankMessage(ThanksMessageRequestDto thanksMessageRequestDto, User user);

    default ThkMsgDTO entityToDTO(ThkMsg thkMsg) {
        ThkMsgDTO thkMsgDTO = ThkMsgDTO.builder()
                .feeling(thkMsg.getFeeling())
                .childName(thkMsg.getChildId().getName())
                .restaurantName(thkMsg.getResId().getName())
                .content(thkMsg.getMessage())
                .build();

        return thkMsgDTO;
    }
}
