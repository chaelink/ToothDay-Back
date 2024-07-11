package com.Backend.jwtpart.controller;
import java.util.Date;
import java.util.Map;
import com.Backend.jwtpart.model.User;

import com.Backend.jwtpart.config.jwt.JwtProperties;
import com.Backend.jwtpart.config.oauth.GoogleUser;
import com.Backend.jwtpart.config.oauth.OAuthUserInfo;
import com.Backend.jwtpart.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JwtCreateController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/oauth/jwt/google")
    public String jwtCreate(@RequestBody Map<String, Object> data) {
        System.out.println("jwtCreate 실행됨");
        System.out.println(data.get("profileObj"));
        // OAuthUserInfo 인터페이스를 통해 GoogleUser 객체 생성
        OAuthUserInfo googleUser = new GoogleUser((Map<String, Object>) data.get("profileObj")); //프론트가 보내준 json받기

        // Google OAuth에서 반환하는 사용자 프로필 정보 확인
        System.out.println("Provider: " + googleUser.getProvider());
        System.out.println("ProviderId: " + googleUser.getProviderId());
        System.out.println("Email: " + googleUser.getEmail());
        System.out.println("Name: " + googleUser.getName());

        User userEntity =
                userRepository.findByUsername(googleUser.getProvider()+"_"+googleUser.getProviderId());

        if(userEntity == null) {
            User userRequest = User.builder()
                    .username(googleUser.getProvider()+"_"+googleUser.getProviderId())
                    .email(googleUser.getEmail())
                    .provider(googleUser.getProvider())
                    .providerId(googleUser.getProviderId())
                    .roles("ROLE_USER")
                    .build();

            userEntity = userRepository.save(userRequest);
        }

        String jwtToken = JWT.create()
                .withSubject(userEntity.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
                .withClaim("id", userEntity.getId())
                .withClaim("username", userEntity.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return jwtToken;
    }

}