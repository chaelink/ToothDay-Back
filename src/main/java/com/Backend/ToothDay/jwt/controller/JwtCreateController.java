package com.Backend.ToothDay.jwt.controller;

import java.util.Map;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.config.oauth.GoogleUser;
import com.Backend.ToothDay.jwt.config.oauth.OAuthUserInfo;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JwtCreateController {

    private final UserRepository userRepository;

    @PostMapping("/oauth/jwt/google")
    public String jwtCreate(@RequestBody Map<String, Object> data) {
        System.out.println("jwtCreate 실행됨");
        System.out.println(data.get("profileObj"));
        // OAuthUserInfo 인터페이스를 통해 GoogleUser 객체 생성
        OAuthUserInfo googleUser = new GoogleUser((Map<String, Object>) data.get("profileObj")); // 프론트가 보내준 json받기

        // Google OAuth에서 반환하는 사용자 프로필 정보 확인
        System.out.println("Provider: " + googleUser.getProvider());
        System.out.println("ProviderId: " + googleUser.getProviderId());
        System.out.println("Email: " + googleUser.getEmail());
        System.out.println("Name: " + googleUser.getName());

        User userEntity =
                userRepository.findByUsername(googleUser.getName());

        if(userEntity == null) {
            User userRequest = User.builder()
                    .username(googleUser.getName())
                    .profileImageUrl(googleUser.getProfileImageUrl())
                    .email(googleUser.getEmail())
                    .provider(googleUser.getProvider())
                    .providerId(googleUser.getProviderId())
                    .roles("ROLE_USER")
                    .build();

            userEntity = userRepository.save(userRequest);
        }

        // JwtUtil을 사용하여 JWT Token 생성
        String jwtToken = JwtUtil.generateToken(userEntity.getUsername(), userEntity.getId());

        return jwtToken;
    }
}
