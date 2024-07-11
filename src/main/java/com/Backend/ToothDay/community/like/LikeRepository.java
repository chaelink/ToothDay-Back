package com.Backend.ToothDay.community.like;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(PostLike like) {
        em.persist(like);
    }

    public void delete(PostLike like) {
        em.remove(like);
    }

    public int countByPostId(int postId) {
        List<PostLike> likes = em.createQuery("select l from PostLike l where l.post.postId=:postId", PostLike.class)
                .setParameter("postId", postId).getResultList();
        return likes.size();
    }

}
