package com.fullmugu.nanumeal.api.service.user;

import com.fullmugu.nanumeal.api.dto.InputUserInfoRequestDto;
import com.fullmugu.nanumeal.api.dto.UserDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.exception.CUserNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserFromReq(HttpServletRequest request) {

        Long id = (Long) request.getAttribute("id");

        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User setUserInfo(User user, InputUserInfoRequestDto inputUserInfoRequestDto) {
        user.setName(inputUserInfoRequestDto.getName());
        user.setNickName(inputUserInfoRequestDto.getNickName());
        user.setAge(inputUserInfoRequestDto.getAge());
        user.setLocation(inputUserInfoRequestDto.getLocation());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public String deleteUser(User user) {
        Long id = user.getId();
        userRepository.deleteUserById(id);

        if (userRepository.findById(id).isPresent()) {
            return "fail";
        }

        return "success";
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }

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
