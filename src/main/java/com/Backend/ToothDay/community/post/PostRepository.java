package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.post.model.Keyword;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.community.post.model.PostKeyword;
import com.Backend.ToothDay.community.post.model.PostKeywordId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Post post, List<Integer> keywordIds) {
        em.persist(post);
        Keyword keyword = em.find(Keyword.class,1);
        PostKeyword postKeyword = new PostKeyword();
        PostKeywordId postKeywordId = new PostKeywordId(post.getId(), keyword.getId());
        postKeyword.setPostKeywordId(postKeywordId);
        postKeyword.setPost(post);
        postKeyword.setKeyword(keyword);
        em.persist(postKeyword);
        post.getPostKeywords().add(postKeyword);
        if(!CollectionUtils.isEmpty(keywordIds)) {
            for (Integer keywordId : keywordIds) {
                Keyword keyword1 = em.find(Keyword.class, keywordId);
                if(keyword1 != null) {
                    PostKeyword postKeyword1 = new PostKeyword();
                    PostKeywordId postKeywordId1 = new PostKeywordId(post.getId(), keywordId);
                    postKeyword1.setPostKeywordId(postKeywordId1);
                    postKeyword1.setPost(post);
                    postKeyword1.setKeyword(keyword1);
                    em.persist(postKeyword1);
                    post.getPostKeywords().add(postKeyword1);
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
         List<PostKeyword> postKeywords = em.createQuery("select e from PostKeyword e where e.keyword.id = :keywordId", PostKeyword.class)
                 .setParameter("keywordId", keywordId).getResultList();
         List<Post> posts =postKeywords.stream().map(PostKeyword::getPost).collect(Collectors.toList());
         return posts;
    }

    public Post findById(long postId) {
        return em.find(Post.class, postId);
    }

    public void delete(Post post) {
        em.remove(em.merge(post));
    }

}
