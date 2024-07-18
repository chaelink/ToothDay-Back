package com.Backend.ToothDay.community.post.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostForm {
    private String title;
    private String content;
    private List<Integer> keywords;
}
