package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.comment.Comment;
import com.Backend.ToothDay.community.like.PostLike;
import com.Backend.ToothDay.jwt.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long id;

    private String title;

    private String content;

    private String image;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> likeList = new ArrayList<>();


    //user 정보
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostKeyword> postKeywords = new ArrayList<>();

}
