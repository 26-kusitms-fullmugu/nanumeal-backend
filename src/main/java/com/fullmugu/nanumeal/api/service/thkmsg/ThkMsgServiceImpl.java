package com.fullmugu.nanumeal.api.service.thkmsg;

import com.fullmugu.nanumeal.api.dto.thkmsg.ThkMsgDTO;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsg;
import com.fullmugu.nanumeal.api.entity.thkmsg.ThkMsgRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ThkMsgServiceImpl implements ThkMsgService {

    private final ThkMsgRepository thkMsgRepository;

//    현재 로그인한 유저 아이디로 가져오기
    @Override
    public List<ThkMsgDTO> getAllMsg(User user) {

        List<ThkMsg> thkMsgList = thkMsgRepository.findByDonateIdAndRestId(user);

        List<ThkMsgDTO> thkMsgDTOList = thkMsgList.stream()
                .map(thkMsg -> entityToDTO(thkMsg)).collect(Collectors.toList());

        return thkMsgDTOList;

    }
}
