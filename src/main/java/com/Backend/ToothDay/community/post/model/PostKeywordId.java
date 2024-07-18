package com.Backend.ToothDay.community.post.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter @Setter @EqualsAndHashCode
@Embeddable
//복합키를 정의하는 클래스
public class PostKeywordId implements Serializable {

    private long postId;
    private long keywordId;

    // 기본 생성자
    public PostKeywordId() {}

    // 생성자
    public PostKeywordId(long postId, int keywordId) {
        this.postId = postId;
        this.keywordId = keywordId;
    }
}
