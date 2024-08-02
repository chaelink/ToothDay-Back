package com.Backend.ToothDay.community.post;
import com.Backend.ToothDay.community.comment.CommentDTO;
import com.Backend.ToothDay.community.image.ImageService;
import com.Backend.ToothDay.community.like.LikeService;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.community.post.model.PostDTO;
import com.Backend.ToothDay.community.post.model.PostForm;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import io.swagger.annotations.*;
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
@Api(description = "커뮤니티 게시글 API") //컨트롤러 설명 작성
public class PostController {

    private final UserRepository userRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final PostService postService;
    private final ImageService imageService;
    private final LikeService likeService;

    @ApiOperation(value = "비유저 커뮤니티 첫화면(무한스크롤)")
    @GetMapping("/community") //비유저 커뮤니티 첫화면 페이징
    public List<PostDTO> NonLoginCommunityMain(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                               @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return postService.getAllPostDTOPaging(limit, offset);
    }

    @ApiOperation(value = "유저 커뮤니티 첫화면(무한스크롤)")
    @GetMapping("/api/community") //유저 커뮤니티 첫화면 페이징
    public List<PostDTO> communityMain(HttpServletRequest request,
                                       @RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<PostDTO> postDTOList = postService.getAllPostDTOPaging(limit, offset);
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
            for(CommentDTO commentDTO : postDTO.getCommentDTOList()) {
                if(commentDTO.getUserId()==userId) {
                    commentDTO.setWrittenByCurrentUser(true);
                } else {
                    commentDTO.setWrittenByCurrentUser(false);
                }
            }
        }

        return postDTOList;
    }

    @GetMapping("/community/search/{keywordId}") //비유저 게시글목록 조회 페이징
    public List<PostDTO> NonLoginCommunityFindByKeywordId(@PathVariable int keywordId,
                                                          @RequestParam(value = "offset", defaultValue = "0") int offset,
                                                          @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return postService.getPostDTOByKeywordIdPaging(keywordId,limit,offset);
    }

    @GetMapping("/api/community/search/{keywordId}") //유저 게시글목록 조회 페이징
    public List<PostDTO> communityFindByKeywordIdPaging(@PathVariable int keywordId,
                                                        HttpServletRequest request,
                                                        @RequestParam(value = "offset", defaultValue = "0") int offset,
                                                        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<PostDTO> postDTOList = postService.getPostDTOByKeywordIdPaging(keywordId, limit, offset);
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
            for(CommentDTO commentDTO : postDTO.getCommentDTOList()) {
                if(commentDTO.getUserId()==userId) {
                    commentDTO.setWrittenByCurrentUser(true);
                } else {
                    commentDTO.setWrittenByCurrentUser(false);
                }
            }
        }
        return postDTOList;
    }

    @GetMapping("/community/search")
    public List<PostDTO> communitySearch(@RequestParam(value = "search") String search,
                                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return postService.getPostDTOByQueryPaging(search, offset, limit);
    }

    @GetMapping("/api/community/search")
    public List<PostDTO> communitySearch(HttpServletRequest request,
                                         @RequestBody String search,
                                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit ) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<PostDTO> postDTOList = postService.getPostDTOByQueryPaging(search, offset, limit);
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
            for(CommentDTO commentDTO : postDTO.getCommentDTOList()) {
                if(commentDTO.getUserId()==userId) {
                    commentDTO.setWrittenByCurrentUser(true);
                } else {
                    commentDTO.setWrittenByCurrentUser(false);
                }
            }
        }
        return postDTOList;
    }


    @GetMapping("/community/upload") //커뮤니티 작성 화면
    public PostForm communityUploadForm() {
        return new PostForm();
    }


    @PostMapping("/community/upload") //게시글 작성
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "postForm", value = "게시글 데이터", required = true, dataType = "Text", defaultValue = "multipart/form-data" ),
//            @ApiImplicitParam(name = "files", value = "게시글 이미지", required = false, dataType = "File", defaultValue = "multipart/form-data")
//    })
    public PostDTO communityUpload(@RequestPart(value = "postForm") PostForm postForm,
                                   @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                   HttpServletRequest request) {
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
        if(files != null) {
            imageService.saveImages(post,files);
        }
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
        for(CommentDTO commentDTO : postDTO.getCommentDTOList()) {
            if(commentDTO.getUserId()==userId) {
                commentDTO.setWrittenByCurrentUser(true);
            } else {
                commentDTO.setWrittenByCurrentUser(false);
            }
        }
        return postDTO;
    }

    @PutMapping("/api/community/{postId}") //게시글 수정
    public PostDTO updatePost(@PathVariable long postId,
                              @RequestPart(value = "postForm") PostForm postForm, @RequestPart(value = "files",required = false) List<MultipartFile> files,
                              HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        Post post = postService.findById(postId);

        if(userId.equals(post.getUser().getId())) {
            post.setTitle(postForm.getTitle());  //새로운 post 정보 설정
            post.setContent(postForm.getContent());
            postKeywordRepository.deleteAllByPostId(postId);  //기존의 postkeyword삭제
            post.getPostKeywords().clear();  //403에러수정
            postService.resave(post,postForm.getKeywords());  //수정 위한 저장 메서드 생성
            if(post.getImageList()!=null) {
                imageService.deleteAllByPostId(postId);
            }
            if(files != null) {
                imageService.saveImages(post,files);
            }
            PostDTO postDTO = postService.getPostDTO(post);
            postDTO.setWrittenByCurrentUser(true);
            if(likeService.findByPostIdAndUserId(postId, userId) != null) {
                postDTO.setLikedByCurrentUser(true);
            } else {
                postDTO.setLikedByCurrentUser(false);
            }
            for(CommentDTO commentDTO : postDTO.getCommentDTOList()) {
                if(commentDTO.getUserId()==userId) {
                    commentDTO.setWrittenByCurrentUser(true);
                } else {
                    commentDTO.setWrittenByCurrentUser(false);
                }
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
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post");
        }
    }

}
