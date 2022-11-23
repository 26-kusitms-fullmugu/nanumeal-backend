package com.fullmugu.nanumeal.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.user.UserService;
import com.fullmugu.nanumeal.exception.CInvalidJwtException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String SECRET;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtHeader = ((HttpServletRequest) request).getHeader(JwtProperties.HEADER_STRING); // Authorization 값 가져오기

        // 유효하지 않으면 그냥 return 처리
        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 제대로 된 형식이면 "Bearer "만큼 떼서 token에 담음
        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        Long id = null;

        // HMAC을 검증하고 id 값 가져오기
        try {
            id = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token)
                    .getClaim("id").asLong();
        } catch (TokenExpiredException e) {
            log.info("토큰 만료");
            throw new CInvalidJwtException("만료된 토큰입니다.", ErrorCode.FORBIDDEN);
        } catch (JWTVerificationException e) {
            log.info("토큰 유효 X");
            throw new CInvalidJwtException("유효하지 않은 토큰입니다.", ErrorCode.FORBIDDEN);
        }

        if (id == null) {
            log.info("id 값이 존재하지 않습니다.");
            throw new CInvalidJwtException("유효하지 않은 토큰입니다.", ErrorCode.FORBIDDEN);
        }

        // Service를 통해 id값이 존재하는지 검증
        else if (userService.getUserById(id) == null) {
            log.info(id + "번 사용자는 존재하지 않습니다.");
            throw new CInvalidJwtException("존재하지 않는 사용자입니다.", ErrorCode.FORBIDDEN);
        } else {
            request.setAttribute("id", id);
            User user = userService.getUserById(id);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
        }

        // filterChain에 값 넘김
        filterChain.doFilter(request, response);
    }
}
