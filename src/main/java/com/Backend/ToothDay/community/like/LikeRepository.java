package com.Backend.ToothDay.community.like;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public int countByPostId(long postId) {
        List<PostLike> likes = em.createQuery("select l from PostLike l where l.post.id=:postId", PostLike.class)
                .setParameter("postId", postId).getResultList();
        return likes.size();
    }

    public PostLike findByPostIdAndUserId(long postId, long userId) {
        try {
            return em.createQuery("select pl from PostLike pl where pl.post.id=:postId and pl.user.id=:userId", PostLike.class)
                    .setParameter("postId", postId)
                    .setParameter("userId", userId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PostLike> findByUserIdPaging(long userId, int limit, int offset) {
        return em.createQuery("select pl from PostLike pl where pl.user.id=:userId order by pl.id DESC ", PostLike.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }



}
