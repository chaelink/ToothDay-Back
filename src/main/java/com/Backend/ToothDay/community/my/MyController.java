package com.Backend.ToothDay.community.my;

import com.Backend.ToothDay.community.my.dto.MyCommentPostDTO;
import com.Backend.ToothDay.community.my.dto.MyLikePostDTO;
import com.Backend.ToothDay.community.my.dto.MyPostDTO;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/mypage/community/post")
    public List<MyPostDTO> getMyPosts(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        return myService.getMyPostDTOs(userId);
    }

    @GetMapping("/mypage/community/likePost")
    public List<MyLikePostDTO> getMyLikePosts(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        return myService.getMyLikePostDTOs(userId);
    }

    @GetMapping("/mypage/community/commentPost")
    public List<MyCommentPostDTO> getMyCommentPosts(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        return myService.getMyCommentPostDTOs(userId);
    }
}
