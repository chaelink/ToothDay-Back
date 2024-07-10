package com.Backend.ToothDay.community.post;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public Post save(Post post, List<Integer> keywordIds) {
        em.persist(post);
        if(keywordIds == null) {
            Keyword keyword = em.find(Keyword.class,1);
            PostKeyword postKeyword = new PostKeyword();
            PostKeywordId postKeywordId = new PostKeywordId(post.getPostId(), keyword.getKeywordId());
            postKeyword.setPostKeywordId(postKeywordId);
            postKeyword.setPost(post);
            postKeyword.setKeyword(keyword);
            em.persist(postKeyword);
            post.getPostKeywords().add(postKeyword);
        }
        else {
            for (Integer keywordId : keywordIds) {
                Keyword keyword = em.find(Keyword.class, keywordId);
                if(keyword != null) {
                    PostKeyword postKeyword = new PostKeyword();
                    PostKeywordId postKeywordId = new PostKeywordId(post.getPostId(), keywordId);
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

        return post;
    }

    public List<Post> findAll() {
        return em.createQuery("from Post", Post.class).getResultList();
    }

    public List<Post> findByKeywordId(int keywordId) {
         return em.createQuery("select e from PostKeyword e where e.keyword = :keywordId", Post.class)
                 .setParameter("keywordId", keywordId)
                 .getResultList();
    }

    public Post findById(int postId) {
        return em.find(Post.class, postId);
    }

    public void delete(Post post) {
        em.remove(em.merge(post));
    }

    public Keyword findKeywordById(int keywordId) {
        return em.find(Keyword.class, keywordId);
    }


}
