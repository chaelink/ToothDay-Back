package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.service.KakaoApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoSearchController {

    private final KakaoApiService kakaoApiService;

    @Autowired
    public KakaoSearchController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/search")
<<<<<<< Updated upstream
    public ResponseEntity<Object> search(
            @RequestParam String query,
            @RequestParam String categoryGroupCode) {

        // Call service to fetch data from Kakao API
        String result = kakaoApiService.searchPlaces(query, categoryGroupCode);
=======
    public ResponseEntity<Object> search(@RequestParam String query) {
        // Call service to fetch data from Kakao API
        String result = kakaoApiService.searchPlaces(query);
>>>>>>> Stashed changes

        // Return ResponseEntity with JSON body and HTTP status
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
