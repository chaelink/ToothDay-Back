package com.Backend.ToothDay.community.comment;

import com.Backend.ToothDay.community.post.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
public class Comment {

    @Id @GeneratedValue
    @Column(name = "commentId")
    private int commentId;

    @JoinColumn(name="postId")
    @ManyToOne
    private Post post;

    private String content;

    private LocalDateTime createDate;

    //user
}
