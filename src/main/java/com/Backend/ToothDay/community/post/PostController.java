package com.Backend.ToothDay.community.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/community/upload")
    public PostForm communityUploadForm() {
        return new PostForm();
    }

    @PostMapping("/community/upload")
    public PostDTO communityUpload(@RequestBody PostForm postForm) {
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setImage(postForm.getImage());
        post.setCreateDate(LocalDateTime.now());
        postService.save(post,postForm.getKeywords());
        return postService.getPostDTO(post);
    }

    @GetMapping("/community")
    public List<PostDTO> communityMain() {
        return postService.getAllPostDTO();
    }


}
