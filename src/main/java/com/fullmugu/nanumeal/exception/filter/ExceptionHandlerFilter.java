package com.fullmugu.nanumeal.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullmugu.nanumeal.exception.CInvalidJwtException;
import com.fullmugu.nanumeal.exception.handler.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CInvalidJwtException e) {
            log.error("Unexpected Exception occurred: {}", e.getMessage(), e);
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
            errorResponse.setMessage(e.getMessage());

            response.setStatus(e.getErrorCode().getStatus());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            String result = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(result);

        }
    }
}
