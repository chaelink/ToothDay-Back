package com.Backend.ToothDay.community.like;

import com.Backend.ToothDay.community.post.PostRepository;
import com.Backend.ToothDay.community.post.PostService;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import lombok.Getter;
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

    @Getter
    public static class LikeResponse {
        private final String message;
        private final int likeCount;

        public LikeResponse(String message, int likeCount) {
            this.message = message;
            this.likeCount = likeCount;
        }
    }

    @PostMapping("/api/community/{postId}/like") //게시글 좋아요
    public LikeResponse like(@PathVariable Long postId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId);

        PostLike isLiked = likeService.findByPostIdAndUserId(postId, userId);
        if (isLiked == null) {
            PostLike postLike = new PostLike();
            postLike.setPost(post);
            postLike.setUser(user);
            likeService.save(postLike);
            int likeCount = likeService.countByPostId(postId);
            LikeResponse likeResponse = new LikeResponse("Like Success",likeCount);
            return likeResponse;
        }
        else {
            likeService.delete(isLiked);
            int likeCount = likeService.countByPostId(postId);
            LikeResponse likeResponse = new LikeResponse("Like Delete Success",likeCount);
            return likeResponse;
        }
    }
    
}
