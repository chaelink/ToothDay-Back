package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.comment.CommentRepository;
import com.Backend.ToothDay.community.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.Backend.ToothDay.jwt.model.User;
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByKeywordId(int keywordId) {
        return postRepository.findByKeywordId(keywordId);
    }

    public Post findById(int postId) {
        return postRepository.findById(postId);
    }

    public void save(Post post, List<Integer> keywordIds) {

        postRepository.save(post, keywordIds);
    }

    public Post update(Post post,List<Integer> keywordIds) {

        postRepository.save(post, keywordIds);
        return post;
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public PostDTO getPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setImage(post.getImage());

        //유저 정보 설정
        User user = post.getUser();
        if (user != null) {
            PostDTO.UserDTO userDTO = new PostDTO.UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setProfileImageUrl(user.getProfileImageUrl());
            userDTO.setEmail(user.getEmail());
            postDTO.setUser(userDTO);
        }
        //postDTO.setKeywords(post.getPostKeywords().stream().map(pk->pk.getKeyword().getKeywordId()).collect(Collectors.toList()));

        List<Integer> keywords = post.getPostKeywords().stream()
                .map(pk -> pk.getKeyword().getId())
                .collect(Collectors.toList());
        postDTO.setKeywords(keywords);

        postDTO.setCreateDate(post.getCreateDate());
        postDTO.setCommentCount(commentRepository.countByPostId(post.getId()));
        postDTO.setLikeCount(likeRepository.countByPostId(post.getId()));
        return postDTO;
    }

    public List<PostDTO> getAllPostDTO() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post->getPostDTO(post)).collect(Collectors.toList());
    }

    public List<PostDTO> getPostDTOByKeywordId(int keywordId) {
        List<Post> posts = postRepository.findByKeywordId(keywordId);
        return posts.stream().map(post->getPostDTO(post)).collect(Collectors.toList());
    }



}
