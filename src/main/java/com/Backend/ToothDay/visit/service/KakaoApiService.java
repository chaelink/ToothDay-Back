package com.Backend.ToothDay.visit.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class KakaoApiService {

    private final String kakaoApiKey;
    private final String kakaoApiUrl;

    public KakaoApiService(@Value("${kakao.api.key}") String kakaoApiKey,
                           @Value("${kakao.api.url}") String kakaoApiUrl) {
        this.kakaoApiKey = kakaoApiKey;
        this.kakaoApiUrl = kakaoApiUrl;
    }

    public List<PlaceInfo> searchPlaces(String query) {
        RestTemplate restTemplate = new RestTemplate();

        // Build URL for Kakao API
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", query+",치과")
                .queryParam("category_group_code", "HP8");

        String uriString = builder.toUriString();

        // Decode the URI string
        String decodedUriString = UriUtils.decode(uriString, "UTF-8");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoApiKey); // "KakaoAK " prefix is needed for authorization
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make HTTP GET request to Kakao API
        ResponseEntity<String> response = restTemplate.exchange(
                decodedUriString,
                HttpMethod.GET,
                entity,
                String.class
        );
        System.out.println(decodedUriString);
        System.out.println(response.getBody());
        // Parse the response body to extract desired parameters
        return parseResponse(response.getBody());
    }

    private List<PlaceInfo> parseResponse(String responseBody) {
        List<PlaceInfo> placeInfoList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode documents = root.path("documents");

            for (JsonNode node : documents) {
                String addressName = node.path("address_name").asText();
                String placeName = node.path("place_name").asText();
                String id = node.path("id").asText();
                placeInfoList.add(new PlaceInfo(addressName, placeName, id));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeInfoList;
    }

    // Inner class to hold place information
    public class PlaceInfo {

        @JsonProperty("id")
        private String id;

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("address_name")
        private String addressName;

        public PlaceInfo(String addressName, String placeName, String id) {
            this.addressName = addressName;
            this.placeName = placeName;
            this.id = id;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "PlaceInfo{" +
                    "addressName='" + addressName + '\'' +
                    ", placeName='" + placeName + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}
