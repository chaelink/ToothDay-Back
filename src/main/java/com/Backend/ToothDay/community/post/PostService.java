package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.comment.CommentRepository;
import com.Backend.ToothDay.community.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

    public Post save(Post post, List<Integer> keywordIds) {
        return postRepository.save(post, keywordIds);
    }

    public Post update(Post post,List<Integer> keywordIds) {
        return postRepository.save(post, keywordIds);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public PostDTO getPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getPostId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setImage(post.getImage());
        postDTO.setKeywords(post.getPostKeywords().stream().map(pk->pk.getKeyword().getKeywordId()).collect(Collectors.toList()));
        postDTO.setCreateDate(post.getCreateDate());
        postDTO.setCommentCount(commentRepository.countByPostId(post.getPostId()));
        postDTO.setLikeCount(likeRepository.countByPostId(post.getPostId()));
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
