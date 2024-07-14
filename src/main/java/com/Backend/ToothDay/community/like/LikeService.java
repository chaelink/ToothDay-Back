package com.Backend.ToothDay.community.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public void save(PostLike like) {
        likeRepository.save(like);
    }

    public void delete(PostLike like) {
        likeRepository.delete(like);
    }

    public int countByPostId(long postId) {
        return likeRepository.countByPostId(postId);
    }

    public PostLike findByPostIdAndUserId(long postId, long userId) {
        return likeRepository.findByPostIdAndUserId(postId, userId);
    }

    public List<PostLike> findByuserId(long userId) {
        return likeRepository.findByUserId(userId);
    }


}
