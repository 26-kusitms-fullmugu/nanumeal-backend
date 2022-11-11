package com.fullmugu.nanumeal.api.service;

import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User getUser(HttpServletRequest request) {

        Long id = (Long) request.getAttribute("id");

        return userRepository.findById(id).orElseThrow();
    }

    public User getUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }
}
