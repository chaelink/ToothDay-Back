package com.Backend.ToothDay.notification.controller;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.notification.dto.NotificationDTO;
import com.Backend.ToothDay.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<?> getNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Authorization 헤더에서 토큰을 추출
            String token = authorizationHeader.replace("Bearer ", "").trim();
            // 토큰에서 사용자 ID를 추출
            Long userId = JwtUtil.getUserIdFromToken(token);
            // 가장 최근 10개의 알림을 조회하고 DTO로 변환
            List<NotificationDTO> notifications = notificationService.getRecentNotifications(userId, 10);
            // DTO 리스트를 응답으로 반환
            return ResponseEntity.ok(notifications);
        } catch (IllegalArgumentException e) {
            // JWT 토큰이 잘못된 경우
            System.err.println("Invalid token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        } catch (Exception e) {
            // 예외 발생 시 스택 트레이스 출력 및 오류 응답
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving notifications");
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Authorization 헤더에서 토큰을 추출
            String token = authorizationHeader.replace("Bearer ", "").trim();
            // 토큰에서 사용자 ID를 추출
            Long userId = JwtUtil.getUserIdFromToken(token);
            // 알림을 읽음 상태로 변경
            notificationService.markAsRead(notificationId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // JWT 토큰이 잘못된 경우
            System.err.println("Invalid token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        } catch (Exception e) {
            // 예외 발생 시 스택 트레이스 출력 및 오류 응답
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking notification as read");
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Authorization 헤더에서 토큰을 추출
            String token = authorizationHeader.replace("Bearer ", "").trim();
            // 토큰에서 사용자 ID를 추출
            Long userId = JwtUtil.getUserIdFromToken(token);
            // 알림을 삭제
            notificationService.deleteNotification(notificationId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // JWT 토큰이 잘못된 경우
            System.err.println("Invalid token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        } catch (Exception e) {
            // 예외 발생 시 스택 트레이스 출력 및 오류 응답
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting notification");
        }
    }
}
