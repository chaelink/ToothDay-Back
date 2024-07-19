package com.Backend.ToothDay.community.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Keyword {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private int id;

    @Enumerated(EnumType.STRING)
    private KeywordName keywordName;

    @OneToMany(mappedBy = "keyword",cascade = CascadeType.ALL)   //꼭 필요한가?
    @JsonIgnore // 순환 참조 방지
    private List<PostKeyword> postKeywords = new ArrayList<>();
}
