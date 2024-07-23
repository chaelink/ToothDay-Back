package com.Backend.ToothDay.community.comment;

import com.Backend.ToothDay.community.like.LikeService;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.community.post.model.PostDTO;
import com.Backend.ToothDay.community.post.PostService;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping("/api/community/{postId}/comment")
    public PostDTO commentUpload(@PathVariable long postId, @RequestParam String content, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);

        // userId를 이용하여 User 객체를 가져옵니다.
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        Post post = postService.findById(postId);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        commentService.save(comment);
        PostDTO postDTO = postService.getPostDTO(post);
        if(postDTO.getUser().getId()==userId) {
            postDTO.setWrittenByCurrentUser(true);
        } else {
            postDTO.setWrittenByCurrentUser(false);
        }
        if(likeService.findByPostIdAndUserId(postId, userId)!=null) {
            postDTO.setLikedByCurrentUser(true);
        } else {
            postDTO.setLikedByCurrentUser(false);
        }
        for (CommentDTO commentDTO : postDTO.getCommentDTOList()) {
            if(commentDTO.getUserId()==userId) {
                commentDTO.setWrittenByCurrentUser(true);
            } else {
                commentDTO.setWrittenByCurrentUser(false);
            }
        }
        return postDTO;
    }

    @GetMapping("/api/community/post/{commentId}")
    public ResponseEntity<String> commentDelete(@PathVariable long commentId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        Comment comment = commentService.findById(commentId);
        Post post = postService.findById(comment.getPost().getId());
        User userWrote = comment.getUser();
        if(userId.equals(userWrote.getId())) {
            commentService.delete(comment);
        } else {
            throw new RuntimeException("수정/삭제는 댓글 작성자만 가능합니다");
        }
        return ResponseEntity.ok("comment deleted successfully");
    }


}
