package com.Backend.ToothDay.community.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class PostKeyword {

    @EmbeddedId //복합키를 사용하기 위해 PostKeywordId 선언
    private PostKeywordId postKeywordId;

    @ManyToOne
    @MapsId("postId") //복합 키 매핑시 사용하는 애노테이션
    @JoinColumn(name = "post_id")
    @JsonIgnore // 순환 참조 방지
    private Post post;

    @ManyToOne
    @MapsId("keywordId")
    @JoinColumn(name = "keyword_id")
    @JsonIgnore // 순환 참조 방지
    private Keyword keyword;
}
