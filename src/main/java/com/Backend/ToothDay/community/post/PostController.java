package com.Backend.ToothDay.community.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/community/upload")
    public String createPostForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "community/upload";  //(반환할 뷰 이름)프론트와 경로가 일치해야하니까 API명세에 추가해야하나?
    }

    @PostMapping("/community/upload")
    public String communityPostUpload(PostForm postForm, Model model) {
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setImage(postForm.getImage());
        postService.save(post,postForm.getKeywords());
        model.addAttribute("postDTO",postService.getPostDTO(post));
        return "community/upload";
    }


    @GetMapping("/community")
    public String communityMain(Model model) {
        model.addAttribute("List<postDTO>",postService.getAllPostDTO());
        return "community";
    }


}
