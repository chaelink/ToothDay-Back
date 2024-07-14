package com.Backend.ToothDay.community.my.dto;

import com.Backend.ToothDay.jwt.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MyCommentPostDTO {
    private long postId;
    private LocalDateTime createDate;
    private String title;
    private String content;
    private List<String> imageUrl;
    private List<Integer> keywords;
    private String myComment;
    private UserDTO user;
}
