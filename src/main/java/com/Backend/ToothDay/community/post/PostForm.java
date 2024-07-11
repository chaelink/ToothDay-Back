package com.Backend.ToothDay.community.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostForm {
    private String title;
    private String content;
    private String image;
    private List<Integer> keywords;
}
