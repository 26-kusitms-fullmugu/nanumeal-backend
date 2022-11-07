package com.fullmugu.nanumeal.api.service;

import com.fullmugu.nanumeal.api.dto.UserDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.exception.CUserNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CUserNotFoundException(
                        "존재하지 않는 회원입니다.", ErrorCode.NOT_FOUND
                ));
        UserDTO userDTO = entityToDto(user);
        return userDTO;

    }
}
