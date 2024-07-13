package com.Backend.ToothDay.community.post;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class PostKeywordRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void deleteAllByPostId(Long postId) {
        em.createQuery("DELETE FROM PostKeyword pk WHERE pk.post.id = :postId")
                .setParameter("postId", postId).executeUpdate();
    }
}
