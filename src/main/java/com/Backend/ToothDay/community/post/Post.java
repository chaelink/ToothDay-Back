package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.comment.Comment;
import com.Backend.ToothDay.community.like.PostLike;
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
    @Column(name = "postId")
    private int postId;

    private String title;

    private String content;

    private String image;

    //@Transient
    //private List<Integer> keywords;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> likeList = new ArrayList<>();


    //user 정보


    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostKeyword> postKeywords = new ArrayList<>();

    //public void setKeywords() {
     //   this.keywords = this.postKeywords.stream()
       //         .map(pk -> pk.getPostKeywordId().getKeywordId())
       //         .collect(Collectors.toList());
   // }
}
