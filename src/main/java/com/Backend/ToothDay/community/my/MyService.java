package com.Backend.ToothDay.community.my;

import com.Backend.ToothDay.community.comment.Comment;
import com.Backend.ToothDay.community.comment.CommentService;
import com.Backend.ToothDay.community.like.LikeService;
import com.Backend.ToothDay.community.like.PostLike;
import com.Backend.ToothDay.community.my.dto.MyCommentPostDTO;
import com.Backend.ToothDay.community.my.dto.MyLikePostDTO;
import com.Backend.ToothDay.community.my.dto.MyPostDTO;
import com.Backend.ToothDay.community.post.PostRepository;
import com.Backend.ToothDay.community.post.model.Post;
import com.Backend.ToothDay.jwt.dto.UserDTO;
import com.Backend.ToothDay.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MyService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final LikeService likeService;

    public List<MyPostDTO> getMyPostDTOs(long userId, int limit, int offset) {
        List<Post> postList = postRepository.findByUserIdPaging(userId, limit, offset);

        List<MyPostDTO> myPostDTOList = new ArrayList<>();

        for (Post post : postList) {
            MyPostDTO myPostDTO = new MyPostDTO();
            myPostDTO.setPostId(post.getId());
            myPostDTO.setCreateDate(post.getCreateDate());
            myPostDTO.setTitle(post.getTitle());
            myPostDTO.setContent(post.getContent());
            myPostDTO.setImageUrl(getImageUrls(post));
            myPostDTO.setKeywords(getKeywords(post));
            myPostDTO.setCommentCount(commentService.countByPostId(post.getId()));
            myPostDTO.setLikeCount(likeService.countByPostId(post.getId()));

            if(likeService.findByPostIdAndUserId(post.getId(), userId) != null) {
                myPostDTO.setLikedByCurrentUser(true);
            }
            else {
                myPostDTO.setLikedByCurrentUser(false);
            }
            myPostDTOList.add(myPostDTO);
        }
        return myPostDTOList;
    }

    public List<MyLikePostDTO> getMyLikePostDTOs(long userId, int limit, int offset) {
        List<PostLike> postLikeList = likeService.findByuserIdPaging(userId, limit, offset);

        List<MyLikePostDTO> myLikePostDTOList = new ArrayList<>();

        for (PostLike postLike : postLikeList) {
            Post post = postLike.getPost();
            MyLikePostDTO myLikePostDTO = new MyLikePostDTO();
            myLikePostDTO.setPostId(post.getId());
            myLikePostDTO.setCreateDate(post.getCreateDate());
            myLikePostDTO.setTitle(post.getTitle());
            myLikePostDTO.setContent(post.getContent());
            myLikePostDTO.setImageUrl(getImageUrls(post));
            myLikePostDTO.setKeywords(getKeywords(post));
            myLikePostDTO.setCommentCount(commentService.countByPostId(post.getId()));
            myLikePostDTO.setLikeCount(likeService.countByPostId(post.getId()));
            //유저 정보 설정
            User user = post.getUser();
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setUsername(user.getUsername());
                userDTO.setProfileImageUrl(user.getProfileImageUrl());
                userDTO.setEmail(user.getEmail());
                myLikePostDTO.setUser(userDTO);
            }
            myLikePostDTOList.add(myLikePostDTO);
        }
        return myLikePostDTOList;
    }

    public List<MyCommentPostDTO> getMyCommentPostDTOs (long userId, int limit, int offset) {
        List<Comment> commentList = commentService.findByUserIdPaging(userId, limit, offset);

        List<MyCommentPostDTO> myCommentPostDTOList = new ArrayList<>();

        for (Comment comment : commentList) {
            MyCommentPostDTO myCommentPostDTO = new MyCommentPostDTO();
            Post post = comment.getPost();
            myCommentPostDTO.setPostId(post.getId());
            myCommentPostDTO.setCreateDate(post.getCreateDate());
            myCommentPostDTO.setTitle(post.getTitle());
            myCommentPostDTO.setContent(post.getContent());
            myCommentPostDTO.setImageUrl(getImageUrls(post));
            myCommentPostDTO.setKeywords(getKeywords(post));
            myCommentPostDTO.setMyComment(comment.getContent());
            User user = post.getUser();
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setUsername(user.getUsername());
                userDTO.setProfileImageUrl(user.getProfileImageUrl());
                userDTO.setEmail(user.getEmail());
                myCommentPostDTO.setUser(userDTO);
            }
            myCommentPostDTOList.add(myCommentPostDTO);
        }
        return myCommentPostDTOList;
    }


    private List<String> getImageUrls(Post post) {
        List<String> imageUrls = post.getImageList().stream()
                .map(image -> image.getImageUrl()).collect(Collectors.toList());
        return imageUrls;
    }

    private List<Integer> getKeywords(Post post) {
        List<Integer> keywords = post.getPostKeywords().stream()
                .map(pk -> pk.getKeyword().getId())
                .collect(Collectors.toList());
        return keywords;
    }






}
