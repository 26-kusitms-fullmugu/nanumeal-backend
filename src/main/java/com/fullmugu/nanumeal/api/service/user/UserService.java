package com.fullmugu.nanumeal.api.service.user;

import com.fullmugu.nanumeal.api.entity.user.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    User getUser(HttpServletRequest request);

    User getUserById(Long id);
}
