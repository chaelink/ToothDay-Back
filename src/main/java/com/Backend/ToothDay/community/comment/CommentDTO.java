package com.Backend.ToothDay.community.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CommentDTO {
    private long id;
    private String content;
    private LocalDateTime createDate;
    private String username;
    private String profileImageUrl;
}
