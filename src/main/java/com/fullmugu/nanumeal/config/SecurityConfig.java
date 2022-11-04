package com.fullmugu.nanumeal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        /* Spring Security를 적용하지 않을 URI 설정 */
        return web -> {
            web
                    .ignoring()
                    .antMatchers(
                            "/swagger-ui/**"
                    )
            ;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
//                .authenticationEntryPoint()
//                .accessDeniedHandler()
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(
                        "/user/signup",
                        "/user/login",
                        "/auth/**"
                )
                .permitAll()
                .anyRequest().authenticated()
//                .and()
//                .oauth2Login()
//                .authorizationEndpoint()
//                .baseUri("/oauth2/authorization")
//                .authorizationRequestRepository()
        ;

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
