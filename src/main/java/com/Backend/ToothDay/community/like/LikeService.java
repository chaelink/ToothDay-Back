package com.Backend.ToothDay.community.like;

import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.notification.model.Notification;
import com.Backend.ToothDay.notification.service.NotificationService;
import com.Backend.ToothDay.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final WebSocketHandler webSocketHandler; // 웹소켓 핸들러 추가
    private final NotificationService notificationService; // NotificationService 추가

    public void save(PostLike like) {
        likeRepository.save(like);
        Post post = like.getPost();
        User postAuthor = post.getUser();
        User likeAuthor = like.getUser();
        if (!like.getUser().equals(postAuthor)) {
//            String messageContent = "누군가 좋아요를 눌렀어요. " + like.getUser().getUsername();

            // 알림 객체 생성 및 저장
            Notification notification = new Notification();
            notification.setUserId(postAuthor.getId());
            notification.setType("LIKE");
            notification.setPost(post); // Post 객체 설정
            notification.setPostTitle(post.getTitle()); // Post 제목 설정
            notification.setUsername(likeAuthor.getUsername()); // 좋아요 작성자 이름 설정
            notificationService.saveNotification(notification);
            // 웹소켓 알림 전송
            webSocketHandler.sendNotification(postAuthor.getId(), "LIKE", post.getId(), post.getTitle(), likeAuthor.getUsername());
        }
    }
    public void delete(PostLike like) {
        likeRepository.delete(like);
    }

    public int countByPostId(long postId) {
        return likeRepository.countByPostId(postId);
    }

    public PostLike findByPostIdAndUserId(long postId, long userId) {
        return likeRepository.findByPostIdAndUserId(postId, userId);
    }

    public List<PostLike> findByuserIdPaging(long userId, int limit, int offset) {
        return likeRepository.findByUserIdPaging(userId,limit,offset);
    }


}
