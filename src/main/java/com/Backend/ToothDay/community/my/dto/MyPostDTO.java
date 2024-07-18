package com.Backend.ToothDay.community.my.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MyPostDTO {
    private long postId;
    private LocalDateTime createDate;
    private String title;
    private String content;
    private List<String> imageUrl;
    private List<Integer> keywords;
    private int commentCount;
    private int likeCount;
    private boolean isLikedByCurrentUser;
}
