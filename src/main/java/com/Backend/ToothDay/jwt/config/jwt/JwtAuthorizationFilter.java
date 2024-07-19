package com.Backend.ToothDay.jwt.config.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Backend.ToothDay.jwt.config.auth.PrincipalDetails;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

// 인가
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("header Authorization : " + header);

        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

        // JwtUtil을 사용하여 Token에서 Username을 추출
        Long userId = JwtUtil.getUserIdFromToken(token);


        // User ID가 null이 아닌 경우에만 처리
        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);

            // User가 존재할 경우, 인증 처리
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                PrincipalDetails principalDetails = new PrincipalDetails(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,
                        null,
                        principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // User가 존재하지 않을 경우, 적절한 로그 처리 또는 예외 처리
                System.out.println("User not found with ID: " + userId);
                // 예를 들어, 인증 실패 응답을 반환할 수 있음
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // 다음 필터로 이동
        chain.doFilter(request, response);
    }
}