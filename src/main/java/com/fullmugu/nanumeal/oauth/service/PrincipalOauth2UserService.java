package com.fullmugu.nanumeal.oauth.service;

import com.fullmugu.nanumeal.api.entity.user.Role;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.entity.user.UserRepository;
import com.fullmugu.nanumeal.oauth.entity.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();    //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;            // 사용자가 입력한 적은 없지만 만들어준다

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("패스워드" + uuid);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2User.getAttribute("email");
        Role role = Role.ROLE_USER;

        User byUsername = userRepository.findByEmail(username);

        //DB에 없는 사용자라면 회원가입처리
        if (byUsername == null) {
            byUsername = User.oauth2Register()
                    .email(email)
                    .password(password)
                    .role(role)
                    .build();
            userRepository.save(byUsername);
        }

        return new PrincipalDetails(byUsername, oAuth2User.getAttributes());
    }
}

