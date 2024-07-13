package com.Backend.ToothDay.community.like;

import com.Backend.ToothDay.community.post.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter @Setter
@Entity
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private long id;

    @JoinColumn(name = "post_id")
    @ManyToOne
    private Post post;

    //user
}
