package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.service.KakaoApiService;
import com.Backend.ToothDay.visit.service.KakaoApiService.PlaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KakaoSearchController {

    private final KakaoApiService kakaoApiService;

    @Autowired
    public KakaoSearchController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/search")
    public List<PlaceInfo> searchPlaces(@RequestParam String query) {
        return kakaoApiService.searchPlaces(query);
    }
}
