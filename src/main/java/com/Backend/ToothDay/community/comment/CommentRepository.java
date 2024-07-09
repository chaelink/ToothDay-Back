package com.Backend.ToothDay.community.comment;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CommentRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public void delete(Comment comment) {
        em.remove(em.merge(comment));
    }

    public int countByPostId(int postId) {
        Long count = (Long) em.createQuery("select c from Comment c where c.post.postId = :postId")
                .setParameter("postId", postId).getSingleResult();
        return count.intValue();
    }

}
