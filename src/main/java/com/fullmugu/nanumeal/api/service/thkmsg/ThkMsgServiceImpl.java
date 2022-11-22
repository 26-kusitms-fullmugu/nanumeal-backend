package com.fullmugu.nanumeal.api.service.thkmsg;

import com.fullmugu.nanumeal.api.dto.thkmsg.ThanksMessageRequestDto;
import com.fullmugu.nanumeal.api.dto.thkmsg.ThkMsgDTO;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.restaurant.RestaurantRepository;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsg;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsgRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.exception.CRestaurantNotFoundException;
import com.fullmugu.nanumeal.exception.CUserNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ThkMsgServiceImpl implements ThkMsgService {

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    private final ThkMsgRepository thkMsgRepository;

    //    현재 로그인한 유저 아이디로 가져오기
    @Override
    public List<ThkMsgDTO> getAllMsg(User user) {

        List<ThkMsg> thkMsgList = thkMsgRepository.findByDonateIdAndRestId(user);

        List<ThkMsgDTO> thkMsgDTOList = thkMsgList.stream()
                .map(thkMsg -> entityToDTO(thkMsg)).collect(Collectors.toList());

        return thkMsgDTOList;

    }

    @Override
    @Transactional
    public ThkMsg makeThankMessage(ThanksMessageRequestDto thanksMessageRequestDto, User user) {
        if (user == null) {
            throw new CUserNotFoundException("유효하지 않은 사용자입니다.", ErrorCode.FORBIDDEN);
        }

        Restaurant restaurant = restaurantRepository
                .findByNameAndLocation(thanksMessageRequestDto.getRestaurantName(), thanksMessageRequestDto.getRestaurantLocation())
                .orElseThrow(() -> new CRestaurantNotFoundException("식당 이름이나 위치가 올바르지 않습니다.", ErrorCode.BAD_REQUEST));

        return thkMsgRepository.save(
                ThkMsg
                        .builder()
                        .childId(user)
                        .resId(restaurant)
                        .feeling(thanksMessageRequestDto.getFeeling())
                        .message(thanksMessageRequestDto.getMessage())
//                        .regDate(LocalDateTime.now())
                        .build()
        );
    }
}
