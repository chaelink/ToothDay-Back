package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.visit.model.Dentist;
import com.Backend.ToothDay.visit.repository.DentistRepository;
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
    private final DentistRepository dentistRepository; // 레포지토리 주입

    public KakaoApiService(@Value("${kakao.api.key}") String kakaoApiKey,
                           @Value("${kakao.api.url}") String kakaoApiUrl,
                           DentistRepository dentistRepository) { // 생성자 주입
        this.kakaoApiKey = kakaoApiKey;
        this.kakaoApiUrl = kakaoApiUrl;
        this.dentistRepository = dentistRepository;
    }

    public List<PlaceInfo> searchPlaces(String query) {
        RestTemplate restTemplate = new RestTemplate();

        // Kakao API URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", query + ",치과")
                .queryParam("category_group_code", "HP8");

        String uriString = builder.toUriString();

        // URI 문자열 디코딩
        String decodedUriString = UriUtils.decode(uriString, "UTF-8");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",  kakaoApiKey); // 인증을 위한 "KakaoAK " 접두사 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Kakao API에 HTTP GET 요청
        ResponseEntity<String> response = restTemplate.exchange(
                decodedUriString,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 응답 본문을 파싱하여 필요한 데이터 추출
        List<PlaceInfo> placeInfoList = parseResponse(response.getBody());

        // PlaceInfo 데이터를 데이터베이스에 저장
        savePlacesToDatabase(placeInfoList);

        return placeInfoList;
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

    private void savePlacesToDatabase(List<PlaceInfo> placeInfoList) {
        for (PlaceInfo placeInfo : placeInfoList) {
            Dentist dentist = Dentist.builder()
                    .dentistId(Long.parseLong(placeInfo.getId())) // id가 고유하고 올바르게 파싱되었는지 확인
                    .dentistName(placeInfo.getPlaceName())
                    .dentistAddress(placeInfo.getAddressName())
                    .build();

            dentistRepository.save(dentist);
        }
    }

    // 내부 클래스: 장소 정보
    public static class PlaceInfo {

        private String id;
        private String placeName;
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
