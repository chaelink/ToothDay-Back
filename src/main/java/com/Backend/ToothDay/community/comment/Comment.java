package com.Backend.ToothDay.community.comment;

import com.Backend.ToothDay.community.post.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    @JoinColumn(name="post_id")
    @ManyToOne
    private Post post;

    private String content;

    private LocalDateTime createDate;

    //user
}
