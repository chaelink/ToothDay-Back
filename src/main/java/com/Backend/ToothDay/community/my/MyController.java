package com.Backend.ToothDay.community.my;

import com.Backend.ToothDay.community.my.dto.MyCommentPostDTO;
import com.Backend.ToothDay.community.my.dto.MyLikePostDTO;
import com.Backend.ToothDay.community.my.dto.MyPostDTO;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/mypage/community/post")
    public List<MyPostDTO> getMyPosts(HttpServletRequest request,
                                      @RequestParam(value = "offset", defaultValue = "0") int offset,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        return myService.getMyPostDTOs(userId,limit,offset);
    }

    @GetMapping("/mypage/community/likePost")
    public List<MyLikePostDTO> getMyLikePosts(HttpServletRequest request,
                                              @RequestParam(value = "offset", defaultValue = "0") int offset,
                                              @RequestParam(value = "limit", defaultValue = "10") int limit) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        return myService.getMyLikePostDTOs(userId,limit,offset);
    }

    @GetMapping("/mypage/community/commentPost")
    public List<MyCommentPostDTO> getMyCommentPosts(HttpServletRequest request,
                                                    @RequestParam(value = "offset", defaultValue = "0") int offset,
                                                    @RequestParam(value = "limit", defaultValue = "10") int limit) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = JwtUtil.getUserIdFromToken(token);
        return myService.getMyCommentPostDTOs(userId, limit, offset);
    }
}
