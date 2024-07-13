package com.Backend.ToothDay.community.post;
import com.Backend.ToothDay.community.image.ImageService;
import com.Backend.ToothDay.community.like.LikeService;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.community.post.model.PostDTO;
import com.Backend.ToothDay.community.post.model.PostForm;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final UserRepository userRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final PostService postService;
    private final ImageService imageService;
    private final LikeService likeService;

    @GetMapping("/community") //비유저 커뮤니티 첫화면
    public List<PostDTO> NonLoginCommunityMain() {
        return postService.getAllPostDTO();
    }

    @GetMapping("/api/community") //유저 커뮤니티 첫화면
    public List<PostDTO> communityMain(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<PostDTO> postDTOList = postService.getAllPostDTO();
        for (PostDTO postDTO : postDTOList) {
            if(likeService.findByPostIdAndUserId(postDTO.getPostId(), userId) != null) {
                postDTO.setLikedByCurrentUser(true);
            } else {
                postDTO.setLikedByCurrentUser(false);
            }
            if(postDTO.getUser().getId()==userId) {
                postDTO.setWrittenByCurrentUser(true);
            } else {
                postDTO.setWrittenByCurrentUser(false);
            }
        }
        return postDTOList;
    }

    @GetMapping("/community/search/{keywordId}") //비유저 게시글 조회
    public List<PostDTO> NonLoginCommunityFindByKeywordId(@PathVariable int keywordId) {
        return postService.getPostDTOByKeywordId(keywordId);
    }

    @GetMapping("/api/community/search/{keywordId}") //유저 게시글 조회
    public List<PostDTO> communityFindByKeywordId(@PathVariable int keywordId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<PostDTO> postDTOList = postService.getPostDTOByKeywordId(keywordId);
        for (PostDTO postDTO : postDTOList) {
            if(likeService.findByPostIdAndUserId(postDTO.getPostId(), userId) != null) {
                postDTO.setLikedByCurrentUser(true);
            } else {
                postDTO.setLikedByCurrentUser(false);
            }
            if(postDTO.getUser().getId()==userId) {
                postDTO.setWrittenByCurrentUser(true);
            } else {
                postDTO.setWrittenByCurrentUser(false);
            }
        }
        return postDTOList;
    }

    @GetMapping("/community/upload") //커뮤니티 작성 화면
    public PostForm communityUploadForm() {
        return new PostForm();
    }

    @PostMapping("/community/upload") //게시글 작성
    public PostDTO communityUpload(@RequestPart(value = "postForm") PostForm postForm, @RequestPart(value = "files",required = false) List<MultipartFile> files, HttpServletRequest request) {
        // JWT 토큰에서 userId 추출
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);

        // userId를 이용하여 User 객체를 가져옵니다.
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setCreateDate(LocalDateTime.now());
        post.setUser(user);  // 게시글 작성자 설정
        postService.save(post,postForm.getKeywords());
        imageService.saveImages(post,files);
        PostDTO postDTO = postService.getPostDTO(post);
        postDTO.setWrittenByCurrentUser(true);
        return postDTO;
    }

    @GetMapping("/community/{postId}") // 비유저 게시글 열람
    public PostDTO NonLoginPostDetail(@PathVariable long postId) {
        Post post = postService.findById(postId);
        return postService.getPostDTO(post);
    }

    @GetMapping("/api/community/{postId}") //유저 게시글 열람
    public PostDTO postDetail(@PathVariable long postId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);

        Post post = postService.findById(postId);
        PostDTO postDTO = postService.getPostDTO(post);
        if(likeService.findByPostIdAndUserId(postId, userId) != null) {
            postDTO.setLikedByCurrentUser(true);
        } else {
            postDTO.setLikedByCurrentUser(false);
        }
        if(postDTO.getUser().getId()==userId) {
            postDTO.setWrittenByCurrentUser(true);
        } else {
            postDTO.setWrittenByCurrentUser(false);
        }
        return postDTO;
    }

    @PutMapping("/api/community/{postId}") //게시글 수정
    public PostDTO updatePost(@PathVariable long postId, @RequestPart(value = "postForm") PostForm postForm, @RequestPart(value = "files",required = false) List<MultipartFile> files, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        Post post = postService.findById(postId);

        if(userId.equals(post.getUser().getId())) {
            post.setTitle(postForm.getTitle());
            post.setContent(postForm.getContent());
            postKeywordRepository.deleteAllByPostId(postId);
            postService.save(post,postForm.getKeywords());
            imageService.deleteAllByPostId(postId);
            imageService.saveImages(post,files);
            PostDTO postDTO = postService.getPostDTO(post);
            postDTO.setWrittenByCurrentUser(true);
            if(likeService.findByPostIdAndUserId(postId, userId) != null) {
                postDTO.setLikedByCurrentUser(true);
            } else {
                postDTO.setLikedByCurrentUser(false);
            }
            return postDTO;
        }
        else {
            throw new RuntimeException("게시글 작성자 본인만 수정 가능합니다");
        }
    }

    @DeleteMapping("/api/community/{postId}") //게시글 삭제
    public ResponseEntity<String> deletePost(@PathVariable long postId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        Post post = postService.findById(postId);

        if(userId.equals(post.getUser().getId())) {
            postService.delete(post);
            postKeywordRepository.deleteAllByPostId(postId);
            imageService.deleteAllByPostId(postId);
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post");
        }
    }

}
