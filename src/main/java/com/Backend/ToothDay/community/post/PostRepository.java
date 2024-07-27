package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.post.model.Keyword;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.community.post.model.PostKeyword;
import com.Backend.ToothDay.community.post.model.PostKeywordId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
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

    public List<Post> findAllPaging(int limit, int offset) {

        return em.createQuery("from Post", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    public List<Post> findByKeywordIdPaging(int keywordId, int limit, int offset) {
        //PostKeyword에서 keywordId로 Post 조회
        String queryStr = "SELECT pk.post FROM PostKeyword pk WHERE pk.keyword.id = :keywordId";
        TypedQuery<Post> query = em.createQuery(queryStr, Post.class)
                .setParameter("keywordId", keywordId)
                .setFirstResult(offset)
                .setMaxResults(limit);

        return query.getResultList();
    }



    public Post findById(long postId) {
        return em.find(Post.class, postId);
    }

    public List<Post> findByUserIdPaging(long userId, int limit, int offset) {
        return em.createQuery("select p from Post p where p.user.id = :userId",Post.class)
                .setParameter("userId",userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

//    public List<Post> searchPosts(String query, int limit, int offset) {
//        String formattedQuery = "%" + query + "%";
//        return em.createQuery("select p from Post p where p.title like :query", Post.class)
//                .setParameter("query", formattedQuery)
//                .setFirstResult(offset)
//                .setMaxResults(limit)
//                .getResultList();
//    }

    public List<Post> searchPosts(String query, int limit, int offset) {
        String formattedQuery = "%" + query + "%";
        log.debug("Executing search with query: {}", formattedQuery);
        List<Post> results = em.createQuery("select p from Post p where p.title like :query", Post.class)
                .setParameter("query", formattedQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        log.debug("Search results: {}", results);
        return results;
    }

    public List<Post> search(String search, int limit, int offset) {
        return em.createQuery("select p from Post p where p.title=: search ",Post.class)
                .setParameter("search",search)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public void delete(Post post) {
        em.remove(em.merge(post));
    }

}
