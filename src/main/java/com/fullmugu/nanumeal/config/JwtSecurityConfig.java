package com.fullmugu.nanumeal.config;

import com.fullmugu.nanumeal.api.service.UserService;
import com.fullmugu.nanumeal.oauth.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final UserService userService;

    @Override
    public void configure(HttpSecurity http) {
        JwtRequestFilter customFilter = new JwtRequestFilter(userService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
