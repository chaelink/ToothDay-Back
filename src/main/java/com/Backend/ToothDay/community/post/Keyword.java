package com.Backend.ToothDay.community.post;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Keyword {

    @Id @GeneratedValue
    @Column(name = "keywordId")
    private int keywordId;

    @Enumerated(EnumType.STRING)
    private KeywordName keywordName;

    @OneToMany(mappedBy = "keyword",cascade = CascadeType.ALL)   //꼭 필요한가?
    private List<PostKeyword> postKeywords = new ArrayList<>();
}
