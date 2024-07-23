package com.Backend.ToothDay.jwt.config.jwt;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Backend.ToothDay.jwt.config.auth.PrincipalDetails;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 모든 요청의 헤더를 로그로 기록
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                logger.info("Header Name: " + headerName + ", Value: " + request.getHeader(headerName));
            }
            String header = request.getHeader(JwtProperties.HEADER_STRING);
            logger.info("Request URL: " + request.getRequestURL());
            logger.info("Request Method: " + request.getMethod());
            logger.info("Request Header Authorization: " + header);

            if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
                chain.doFilter(request, response);
                return;
            }

            String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

            // JwtUtil을 사용하여 Token에서 Username을 추출
            Long userId = JwtUtil.getUserIdFromToken(token);

            if (userId != null) {
                Optional<User> optionalUser = userRepository.findById(userId);

                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    PrincipalDetails principalDetails = new PrincipalDetails(user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,
                            null,
                            principalDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("User not found with ID: " + userId);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Exception in JwtAuthorizationFilter", e);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        }
    }
}