package com.Backend.ToothDay.jwt.controller;


import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.dto.UserProfileUpdateRequest;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 프로필 조회 API
    @GetMapping("/api/user/profile")
    public User getProfile(HttpServletRequest httpServletRequest) {
        // JWT 토큰에서 userId 추출
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);

        // userId를 이용하여 User 객체를 가져옵니다.
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return user;
    }


    @PutMapping("/api/user/profile")
    public User updateProfile(@RequestBody UserProfileUpdateRequest request, HttpServletRequest httpServletRequest){
    // JWT 토큰에서 userId 추출
    String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
    Long userId = jwtUtil.getUserIdFromToken(token);

    // userId를 이용하여 User 객체를 가져옵니다.
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 프로필 이미지와 사용자 이름을 수정합니다.
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        // 수정된 User 객체를 저장합니다.
        userRepository.save(user);

        return user;
    }
}

