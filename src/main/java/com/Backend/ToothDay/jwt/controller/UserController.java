package com.Backend.ToothDay.jwt.controller;


import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.dto.UserDTO;
import com.Backend.ToothDay.jwt.dto.UserProfileUpdateRequest;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.Backend.ToothDay.jwt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // 프로필 조회 API
    @GetMapping("/api/user/profile")
    public UserDTO getProfile(HttpServletRequest httpServletRequest) {
        // JWT 토큰에서 userId 추출
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);

        // userId를 이용하여 User 객체를 가져옵니다.
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        return UserDTO.from(user);
    }


    @PutMapping("/api/user/profile")
    public UserDTO updateProfile(@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                              @RequestPart(value ="request",  required = false) String requestJson,
                              HttpServletRequest httpServletRequest) throws IOException {
        // JWT 토큰에서 userId 추출
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);

        // userId를 이용하여 User 객체를 가져옵니다.
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        // 프로필 이미지와 사용자 이름을 수정합니다.
        if (profileImage != null && !profileImage.isEmpty()) {
            String profileImageDir = System.getProperty("user.dir") + File.separator + "profileImage" + File.separator;
            File uploadDirFile = new File(profileImageDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
            File dest = new File(profileImageDir + fileName);
            profileImage.transferTo(dest);

            // 저장된 파일 경로를 User 엔티티에 설정
            user.setProfileImageUrl("/profileImage/" + fileName);
        }

        // 사용자 이름 수정
        if (requestJson != null) {
            // JSON 파싱 (Jackson 라이브러리를 사용합니다)
            ObjectMapper objectMapper = new ObjectMapper();
            UserProfileUpdateRequest request = objectMapper.readValue(requestJson, UserProfileUpdateRequest.class);
            if (request.getUsername() != null) {
                user.setUsername(request.getUsername());
            }
            if (request.isDefaultY()) {
                user.setProfileImageUrl(null);
            }
        }
        // 수정된 User 객체를 저장합니다.
        userRepository.save(user);

        return UserDTO.from(user);
    }

    @Data
    private static class UserProfileUpdateRequest {
        private String username;
        private boolean defaultY;


    }
    @DeleteMapping("/api/user/profile")
    public ResponseEntity<String> deleteUser(HttpServletRequest httpServletRequest) {
        // JWT 토큰에서 userId 추출
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 사용자 및 관련 데이터 삭제
        userService.deleteUser(userId);

        return ResponseEntity.ok("사용자 및 관련 데이터가 성공적으로 삭제되었습니다.");
    }
}