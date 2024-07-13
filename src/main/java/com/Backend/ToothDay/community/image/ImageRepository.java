package com.Backend.ToothDay.community.image;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ImageRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Image image) {
        em.persist(image);
    }

    public void deleteAllByPostId(long postId) {
        em.createQuery("delete from Image i where i.post.id = :postId")
                .setParameter("postId", postId).executeUpdate();
    }
}
