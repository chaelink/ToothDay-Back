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

    @GetMapping("/community") //커뮤니티 첫화면
    public List<PostDTO> communityMain() {
        return postService.getAllPostDTO();
    }

    @GetMapping("/community/{keywordId}") //게시글 조회
    public List<PostDTO> communityFindByKeywordId(@PathVariable int keywordId) {
        return postService.getPostDTOByKeywordId(keywordId);
    }

    @GetMapping("/community/upload") //커뮤니티 작성 화면
    public PostForm communityUploadForm() {
        return new PostForm();
    }

    @PostMapping("/community/upload") //게시글 작성
    public PostDTO communityUpload(@RequestBody PostForm postForm) {
        //현재로그인되어있는유저의ID가져오기
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setImage(postForm.getImage());
        post.setCreateDate(LocalDateTime.now());
        postService.save(post,postForm.getKeywords());
        return postService.getPostDTO(post);
    }





}
