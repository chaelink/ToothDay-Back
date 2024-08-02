package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.VisitListDTO;
import com.Backend.ToothDay.visit.service.VisitListService;
import com.Backend.ToothDay.visit.util.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visit")
public class VisitListController {

    @Autowired
    private VisitListService visitListService;

    @GetMapping
    public List<VisitListDTO> getAllVisitRecords(
            @RequestParam(value = "offset", defaultValue = "0")int offset,
            @RequestParam(value = "limit", defaultValue = "10")int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getAllVisitRecords(token, offset, limit);
    }

    @GetMapping("/categories")
    public List<VisitListDTO> getVisitsByCategories(
            @RequestParam List<Integer> categories,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        // 숫자 카테고리를 한글 카테고리로 변환
        List<String> categoryNames = categories.stream()
                .map(CategoryMapper::getCategoryName)
                .collect(Collectors.toList());
        return visitListService.getVisitsByCategories(categoryNames, token, offset, limit);
    }

    @GetMapping("/{visitId}")
    public VisitListDTO getVisitById(
            @PathVariable Long visitId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getVisitById(visitId, token);
    }

    @GetMapping("/category/{category}")
    public List<VisitListDTO> getVisitsByCategory(
            @PathVariable int category,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String categoryName = CategoryMapper.getCategoryName(category);
        return visitListService.getVisitsByCategory(categoryName, token, offset, limit);
    }
    @GetMapping("/category/1")
    public List<VisitListDTO> getAllVisits(
            @RequestParam(value = "offset", defaultValue = "0")int offset,
            @RequestParam(value = "limit", defaultValue = "10")int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getAllVisits(token, offset, limit);
    }

}
