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

    public List<Comment> findByPostId(long postId) {
        List<Comment> comments = em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId).getResultList();
        return comments;
    }

    public Comment findById(long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findByUserId(long userId) {
        return em.createQuery("select c from Comment c where c.user.id = : userId", Comment.class)
                .setParameter("userId", userId).getResultList();
    }

}
