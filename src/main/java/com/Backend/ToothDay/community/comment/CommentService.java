package com.Backend.ToothDay.community.comment;

import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.notification.model.Notification;
import com.Backend.ToothDay.notification.service.NotificationService;
import com.Backend.ToothDay.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final WebSocketHandler webSocketHandler;
    private final NotificationService notificationService;

    public void save(Comment comment) {
        commentRepository.save(comment);
        User postAuthor = comment.getPost().getUser();
        User commentAuthor = comment.getUser();
        if (!comment.getUser().equals(postAuthor)) {
//            String messageContent = "새로운 댓글이 도착했습니다. " + comment.getUser().getUsername();

            // 알림 객체 생성 및 저장
            Notification notification = new Notification();
            notification.setUserId(postAuthor.getId());
            notification.setType("COMMENT");
            notification.setPost(comment.getPost()); // Post 객체 설정
            notification.setPostTitle(comment.getPost().getTitle()); // Post 제목 설정
            notification.setUsername(commentAuthor.getUsername()); // 댓글 작성자 이름 설정
            notificationService.saveNotification(notification);

            // 웹소켓 알림 전송

            System.out.println("Sending notification to user " + postAuthor.getId());
            webSocketHandler.sendNotification(postAuthor.getId(), "COMMENT", comment.getPost().getId(), comment.getPost().getTitle(), commentAuthor.getUsername());
        }
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public int countByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    public CommentDTO getCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreateDate(comment.getCreateDate());
        commentDTO.setUsername(comment.getUser().getUsername());
        commentDTO.setProfileImageUrl(comment.getUser().getProfileImageUrl());
        return commentDTO;
    }

    public List<CommentDTO> getCommentDTOByPostId(List<Comment> comments) {
        return comments.stream()
                .map(this::getCommentDTO)
                .collect(Collectors.toList());
    }
}
