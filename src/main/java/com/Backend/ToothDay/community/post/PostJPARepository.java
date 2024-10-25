package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostJPARepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);
    //List<Long> findPostIdsByTitleContaining(String keyword);
    @Query("SELECT p.id FROM Post p WHERE p.title LIKE %:query%")
    List<Long> findPostIdsByTitleContaining(@Param("query") String query);

}
