package com.fullmugu.nanumeal.config;

import com.fullmugu.nanumeal.auth.jwt.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtSecurityConfig jwtSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                // rest api이므로 기본설정 안함. 기본설정은 비인증 시 로그인 폼 화면으로 리다이렉트 된다.
                .csrf().disable()
                // rest api 이므로 csrf 보안이 필요 없음. disable
                .formLogin().disable()
                // formLogin 미사용
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // jwt token으로 생성하므로 세션은 필요 없으므로 생성 안함.
                .and()
                .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
//                .antMatchers(
//                        "/"
//                        ,"/auth/**"
//                        ,"/swagger-ui/**"
//                        ,"/swagger-resources/**"
//                        ,"/error"
//                        ,"/h2-console/**"
////                        ,"/**"
//                ).permitAll()
//                // 가입 및 인증 주소는 누구나 접근 가능
//                .anyRequest().hasRole("USER")
                .antMatchers(
                        "/user/**"
                ).authenticated()
                .anyRequest()
                .permitAll()
                // 그 외 나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .apply(jwtSecurityConfig);
//                .accessDeniedHandler()


    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/swagger-ui.html",
                "/h2-console/**");
    }

}
