package com.Backend.ToothDay.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //config.addAllowedOrigin("http://localhost:3000"); // 로컬 개발 환경 URL 추가
        //config.addAllowedOrigin("https://toothday.swygbro.com/"); // 실제 프론트엔드 URL로 설정
        config.addAllowedOriginPattern("https://toothday.swygbro.com");
        config.addAllowedOriginPattern("http://localhost:3000"); // 로컬 개발 환경 URL
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}