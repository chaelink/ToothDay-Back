package com.Backend.ToothDay.community.post.model;

import com.Backend.ToothDay.community.comment.CommentDTO;
import com.Backend.ToothDay.jwt.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PostDTO {
    private long postId;
    private LocalDateTime createDate;
    private String title;
    private String content;
    private List<String> imageUrl;
    private List<Integer> keywords;
    private int commentCount;
    private List<CommentDTO> commentDTOList;
    private int likeCount;
    private UserDTO user;
    private boolean isWrittenByCurrentUser;
    private boolean isLikedByCurrentUser;

}
