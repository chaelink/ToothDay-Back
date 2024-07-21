package com.Backend.ToothDay.community.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> findByUserIdPaging(Long userId, int limit, int offset) { return commentRepository.findByUserIdPaging(userId, limit, offset); }

    public int countByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    public CommentDTO getCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreateDate(comment.getCreateDate());
        commentDTO.setUsername(comment.getUser().getUsername());
        commentDTO.setProfileImageUrl(comment.getUser().getProfileImageUrl());
        return commentDTO;
    }

    public List<CommentDTO> getCommentDTOByPostId(List<Comment> comments) {
        return comments.stream().map(comment -> getCommentDTO(comment)).collect(Collectors.toList());
    }
}
