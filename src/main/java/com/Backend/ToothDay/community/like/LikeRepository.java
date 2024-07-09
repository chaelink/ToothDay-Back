package com.Backend.ToothDay.community.like;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Like like) {
        em.persist(like);
    }

    public void delete(Like like) {
        em.remove(like);
    }

    public int countByPostId(int postId) {
        Long count = (Long) em.createQuery("select l from Like l where l.post.postId=:postId")
                .setParameter("postId", postId).getSingleResult();
        return count.intValue();
    }

}
