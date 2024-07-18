package com.Backend.ToothDay.visit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
<<<<<<< Updated upstream
=======
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
>>>>>>> Stashed changes

@Service
public class KakaoApiService {

    private final String kakaoApiKey;
    private final String kakaoApiUrl;

    public KakaoApiService(@Value("${kakao.api.key}") String kakaoApiKey,
                           @Value("${kakao.api.url}") String kakaoApiUrl) {
        this.kakaoApiKey = kakaoApiKey;
        this.kakaoApiUrl = kakaoApiUrl;
    }

<<<<<<< Updated upstream
    public String searchPlaces(String query, String categoryGroupCode) {
        RestTemplate restTemplate = new RestTemplate();

        // Build URL for Kakao API
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", query)
                .queryParam("category_group_code", categoryGroupCode);
=======
    public String searchPlaces(String query) {
        RestTemplate restTemplate = new RestTemplate();

        // Encode query parameter safely for URL
        String encodedQuery = UriUtils.encode(query, StandardCharsets.UTF_8);

        // Build URL for Kakao API
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", encodedQuery);
>>>>>>> Stashed changes

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
