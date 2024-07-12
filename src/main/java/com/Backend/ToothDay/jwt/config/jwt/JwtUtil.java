package com.Backend.ToothDay.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    // 토큰 생성
    public static String generateToken(String username, Long userId) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", userId)
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    // 토큰 검증 및 디코딩
    public static DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);
    }

    // 토큰에서 사용자 ID 추출
    public static Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getClaim("id").asLong();
    }

    // 토큰에서 사용자 이름 추출
    public static String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getClaim("username").asString();
    }
}
