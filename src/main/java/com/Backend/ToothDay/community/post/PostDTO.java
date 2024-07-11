package com.Backend.ToothDay.community.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PostDTO {
    private long postId;
    private String title;
    private String content;
    private String image;
    private List<Integer> keywords;
    private LocalDateTime createDate;
    private int commentCount;
    private int likeCount;

    //private boolean isLikedByCurrentUser;
    //private int userId;
    //private String username;
    //private String profileImage;
}
