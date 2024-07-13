package com.Backend.ToothDay.jwt.dto;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String username;
    private String profileImageUrl;
}