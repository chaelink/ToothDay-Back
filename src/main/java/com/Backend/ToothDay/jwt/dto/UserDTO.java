package com.Backend.ToothDay.jwt.dto;

import com.Backend.ToothDay.jwt.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private long id;
    private String username;
    private String profileImageUrl;
    private String email;

    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername()) // 이 부분에서 null 체크를 할 수도 있습니다.
                .profileImageUrl(user.getProfileImageUrl())
                .email(user.getEmail())
                .build();
    }
}
