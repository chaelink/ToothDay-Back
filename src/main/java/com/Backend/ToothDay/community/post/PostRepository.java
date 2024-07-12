package com.Backend.ToothDay.community.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Post post, List<Integer> keywordIds) {
        em.persist(post);
        if(CollectionUtils.isEmpty(keywordIds)) {
            Keyword keyword = em.find(Keyword.class,1);
            if (keyword == null) {
                throw new NullPointerException("Keyword with ID 1 is null");
            }
            PostKeyword postKeyword = new PostKeyword();
            PostKeywordId postKeywordId = new PostKeywordId(post.getId(), keyword.getId());
            postKeyword.setPostKeywordId(postKeywordId);
            postKeyword.setPost(post);
            postKeyword.setKeyword(keyword);
            em.persist(postKeyword);
            post.getPostKeywords().add(postKeyword);
        }
        else {
            Keyword keyword1 = em.find(Keyword.class,1);
            if (keyword1 == null) {
                throw new NullPointerException("Keyword with ID 1 is null");
            }
            PostKeyword postKeyword1 = new PostKeyword();
            PostKeywordId postKeywordId1 = new PostKeywordId(post.getId(), keyword1.getId());
            postKeyword1.setPostKeywordId(postKeywordId1);
            postKeyword1.setPost(post);
            postKeyword1.setKeyword(keyword1);
            em.persist(postKeyword1);
            post.getPostKeywords().add(postKeyword1);
            for (Integer keywordId : keywordIds) {
                Keyword keyword = em.find(Keyword.class, keywordId);
                if(keyword != null) {
                    PostKeyword postKeyword = new PostKeyword();
                    PostKeywordId postKeywordId = new PostKeywordId(post.getId(), keywordId);
                    postKeyword.setPostKeywordId(postKeywordId);
                    postKeyword.setPost(post);
                    postKeyword.setKeyword(keyword);
                    em.persist(postKeyword);
                    post.getPostKeywords().add(postKeyword);
                }
                else {
                    throw new IllegalArgumentException("Keyword with ID " + keywordId + " not found");
                }
            }
        }
    }

    public List<Post> findAll() {
        return em.createQuery("from Post", Post.class).getResultList();
    }

    public List<Post> findByKeywordId(int keywordId) {
         return em.createQuery("select e from PostKeyword e where e.keyword = :keywordId", Post.class)
                 .setParameter("keywordId", keywordId)
                 .getResultList();
    }

    public Post findById(long postId) {
        return em.find(Post.class, postId);
    }

    public void delete(Post post) {
        em.remove(em.merge(post));
    }

    public Keyword findKeywordById(int keywordId) {
        return em.find(Keyword.class, keywordId);
    }


}
