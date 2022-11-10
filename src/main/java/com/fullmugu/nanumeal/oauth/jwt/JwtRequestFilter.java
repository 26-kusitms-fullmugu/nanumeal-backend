package com.fullmugu.nanumeal.oauth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

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
            id = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("id").asLong();

        } catch (TokenExpiredException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING, "토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING, "유효하지 않은 토큰입니다.");
        }

        // 검증 완료 후 userCode로 값 넘김
        request.setAttribute("id", id);

        // filterChain에 값 넘김
        filterChain.doFilter(request, response);
    }
}
