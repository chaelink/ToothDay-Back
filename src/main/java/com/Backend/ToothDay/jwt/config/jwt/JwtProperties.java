package com.Backend.ToothDay.jwt.config.jwt;

public interface JwtProperties {
    String SECRET = "swi3LqRnKfHz7gVl0w9jK!2Qp#T@M1cRf5x%YzPbW8v&UkEo"; // 더 복잡한 비밀값
    int EXPIRATION_TIME = 7200000; // 2시간 (1/1000초 단위)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
