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
                String dentistAddress = node.path("address_name").asText();
                String dentistName = node.path("place_name").asText();
                String dentistId = node.path("id").asText();
                dentistAddress = dentistAddress.replaceAll("(동)([^\\d]*)\\d.*", "$1$2");
                placeInfoList.add(new PlaceInfo(dentistAddress, dentistName, dentistId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeInfoList;
    }

    private void savePlacesToDatabase(List<PlaceInfo> placeInfoList) {
        for (PlaceInfo placeInfo : placeInfoList) {
            Dentist dentist = Dentist.builder()
                    .dentistId(Long.parseLong(placeInfo.getDentistId())) // id가 고유하고 올바르게 파싱되었는지 확인
                    .dentistName(placeInfo.getDentistName())
                    .dentistAddress(placeInfo.getDentistAddress())
                    .build();

            dentistRepository.save(dentist);
        }
    }

    // 내부 클래스: 장소 정보
    public static class PlaceInfo {

        private String dentistId;
        private String dentistName;
        private String dentistAddress;

        public PlaceInfo(String dentistAddress, String dentistName, String dentistId) {
            this.dentistAddress = dentistAddress;
            this.dentistName = dentistName;
            this.dentistId = dentistId;
        }

        public String getDentistAddress() {
            return dentistAddress;
        }

        public void setDentistAddress(String dentistAddress) {
            this.dentistAddress = dentistAddress;
        }

        public String getDentistName() {
            return dentistName;
        }

        public void setDentistName(String dentistName) {
            this.dentistName = dentistName;
        }

        public String getDentistId() {
            return dentistId;
        }

        public void setDentistId(String dentistId) {
            this.dentistId = dentistId;
        }

        @Override
        public String toString() {
            return "PlaceInfo{" +
                    "dentistAddress='" + dentistAddress + '\'' +
                    ", dentistName='" + dentistName + '\'' +
                    ", dentistId='" + dentistId + '\'' +
                    '}';
        }
    }
}
