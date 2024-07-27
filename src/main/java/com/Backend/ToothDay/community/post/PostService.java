package com.Backend.ToothDay.community.post;
import com.Backend.ToothDay.community.comment.CommentService;
import com.Backend.ToothDay.community.like.LikeService;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.community.post.model.PostDTO;
import com.Backend.ToothDay.jwt.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import com.Backend.ToothDay.jwt.model.User;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeService likeService;
    private final CommentService commentService;

    public Post findById(long postId) {
        return postRepository.findById(postId);
    }

    public void save(Post post, List<Integer> keywordIds) { postRepository.save(post, keywordIds); }

    public void resave(Post post, List<Integer> keywordIds) { postRepository.resave(post, keywordIds); }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public PostDTO getPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        List<String> imageUrls = post.getImageList().stream()
                        .map(image -> image.getImageUrl()).collect(Collectors.toList());
        postDTO.setImageUrl(imageUrls);
        //유저 정보 설정
        User user = post.getUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setProfileImageUrl(user.getProfileImageUrl());
            userDTO.setEmail(user.getEmail());
            postDTO.setUser(userDTO);
        }
        List<Integer> keywords = post.getPostKeywords().stream()
                .map(pk -> pk.getKeyword().getId())
                .collect(Collectors.toList());
        postDTO.setKeywords(keywords);
        postDTO.setCreateDate(post.getCreateDate());
        postDTO.setCommentCount(commentService.countByPostId(post.getId()));
        postDTO.setCommentDTOList(commentService.getCommentDTOByPostId(commentService.findByPostId(post.getId())));
        postDTO.setLikeCount(likeService.countByPostId(post.getId()));
        return postDTO;
    }

    public List<PostDTO> getAllPostDTOPaging(int limit, int offset) {
        List<Post> posts = postRepository.findAllPaging(limit,offset);
        return posts.stream().map(post->getPostDTO(post)).collect(Collectors.toList());
    }

    public List<PostDTO> getPostDTOByKeywordIdPaging(int keywordId, int limit, int offset) {
        List<Post> posts = postRepository.findByKeywordIdPaging(keywordId, limit, offset);
        return posts.stream().map(post->getPostDTO(post)).collect(Collectors.toList());
    }

    public List<PostDTO> getPostDTOByQueryPaging(String query, int limit, int offset) {
        List<Post> posts = postRepository.search(query, limit, offset);
        System.out.println(posts);
        return posts.stream().map(post->getPostDTO(post)).collect(Collectors.toList());
    }

}
