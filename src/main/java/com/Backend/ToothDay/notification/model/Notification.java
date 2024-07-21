package com.Backend.ToothDay.notification.model;


import com.Backend.ToothDay.community.post.model.Post;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private Long userId;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 연관된 Post 엔티티
//    private String content;
    private String postTitle;  // 새로운 필드 추가
    private String username;   // 새로운 필드 추가

    @CreationTimestamp
    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "is_read")
    private boolean read; // 알림 읽음 여부
}
