package com.Backend.ToothDay.community.comment;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public int countByPostId(long postId) {
        List<Comment> comments = em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId).getResultList();
        return comments.size();
    }

}
