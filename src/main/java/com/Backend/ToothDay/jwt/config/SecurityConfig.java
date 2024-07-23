package com.Backend.ToothDay.jwt.config;
import com.Backend.ToothDay.jwt.config.jwt.JwtAuthenticationFilter;
import com.Backend.ToothDay.jwt.config.jwt.JwtAuthorizationFilter;
import com.Backend.ToothDay.jwt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter()) // CORS 설정 추가
                .csrf().disable()// CSRF 비활성화 (REST API에선 일반적으로 비활성화)
                // 세션 관리를 Stateless로 설정 (JWT를 사용할 경우)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 기본 로그인 및 HTTP Basic 인증 비활성화
                .formLogin().disable()
                .httpBasic().disable()
                // JWT 필터 추가
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                .anyRequest().permitAll();
                //.requiresChannel().anyRequest().requiresSecure(); // HTTPS 강제 => 배포할 때 수정
    }
}
