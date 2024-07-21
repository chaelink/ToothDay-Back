package com.Backend.ToothDay.notification.service;

import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.Backend.ToothDay.notification.model.Notification;
import com.Backend.ToothDay.notification.dto.NotificationDTO;
import com.Backend.ToothDay.notification.repository.NotificationRepository;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.community.post.PostRepository; // PostRepository 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository; // PostRepository 추가
    private final UserRepository userRepository; // UserRepository 추가

    public void createNotification(Long userId, String type, Long postId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);

        // Post 객체를 가져와서 설정
        Post post = postRepository.findById(postId); // Optional 제거
        if (post != null) { // Null 체크
            notification.setPost(post); // Post 객체 설정
        } else {
            throw new RuntimeException("Post not found"); // Post가 없으면 예외 처리
        }

        notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        notification.setRead(false); // 기본값은 읽지 않은 상태
        notificationRepository.save(notification);
    }


    public List<NotificationDTO> getRecentNotifications(Long userId, int limit) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByTimestampDesc(userId);
        return notifications.stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        notificationRepository.delete(notification);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setType(notification.getType());
        dto.setPostId(notification.getPost() != null ? notification.getPost().getId() : null);
        dto.setPostTitle(notification.getPostTitle()); // Post title 추가
        dto.setUsername(notification.getUsername()); // Username 추가
        dto.setTimestamp(notification.getTimestamp() != null ? notification.getTimestamp().toLocalDateTime() : null);
        dto.setRead(notification.isRead());
        return dto;
    }}