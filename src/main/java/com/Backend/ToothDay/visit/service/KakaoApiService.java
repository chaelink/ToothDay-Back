package com.Backend.ToothDay.visit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
public class KakaoApiService {

    private final String kakaoApiKey;
    private final String kakaoApiUrl;

    public KakaoApiService(@Value("${kakao.api.key}") String kakaoApiKey,
                           @Value("${kakao.api.url}") String kakaoApiUrl) {
        this.kakaoApiKey = kakaoApiKey;
        this.kakaoApiUrl = kakaoApiUrl;
    }

    public String searchPlaces(String query) {
        RestTemplate restTemplate = new RestTemplate();

        // Encode query parameter safely for URL
        String encodedQuery = UriUtils.encode(query, StandardCharsets.UTF_8);

        // Build URL for Kakao API
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", encodedQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make HTTP GET request to Kakao API
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        // Return the response body
        return response.getBody();
    }
}
