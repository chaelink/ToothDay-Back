package com.Backend.ToothDay.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private Long id;
    private Long userId;
    private String type;
    private Long postId;
    private String postTitle;  // 새로운 필드 추가
    private String username;   // 새로운 필드 추가
//    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

}
