package com.Backend.ToothDay.community.like;

import com.Backend.ToothDay.community.post.PostRepository;
import com.Backend.ToothDay.community.post.PostService;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    @PostMapping("/api/community/{postId}/like")
    public ResponseEntity<String> like(@PathVariable Long postId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId);
        PostLike like = new PostLike();
        like.setUser(user);
        like.setPost(post);
        likeService.save(like);
        return ResponseEntity.ok("Like successful");
    }

    @DeleteMapping("/api/community/{postId}/like")
    public ResponseEntity<String> unlike(@PathVariable Long postId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        Post post = postRepository.findById(postId);
        PostLike like = likeService.findByPostIdAndUserId(postId, userId);
        if(like!=null) {
            likeService.delete(like);
            return ResponseEntity.ok("Like Delete successful");
        } else {
            return ResponseEntity.ok("Like not found");
        }
    }
}
