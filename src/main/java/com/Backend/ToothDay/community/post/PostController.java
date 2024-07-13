package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private UserRepository userRepository;

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

    public PostDTO communityUpload(@RequestBody PostForm postForm, HttpServletRequest request) {
        // JWT 토큰에서 userId 추출
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);

        // userId를 이용하여 User 객체를 가져옵니다.
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setImage(postForm.getImage());
        post.setCreateDate(LocalDateTime.now());
        post.setUser(user);  // 게시글 작성자 설정
        postService.save(post,postForm.getKeywords());
        return postService.getPostDTO(post);
    }

}
