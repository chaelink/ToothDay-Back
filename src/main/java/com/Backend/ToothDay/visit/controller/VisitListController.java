package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.VisitListDTO;
import com.Backend.ToothDay.visit.service.VisitListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

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
            @RequestParam List<String> categories,
            @RequestParam(value = "offset", defaultValue = "0")int offset,
            @RequestParam(value = "limit", defaultValue = "10")int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getVisitsByCategories(categories, token, offset, limit);
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
            @PathVariable String category,
            @RequestParam(value = "offset", defaultValue = "0")int offset,
            @RequestParam(value = "limit", defaultValue = "10")int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getVisitsByCategory(category, token, offset, limit);
    }
    @GetMapping("/category/전체")
    public List<VisitListDTO> getAllVisits(
            @RequestParam(value = "offset", defaultValue = "0")int offset,
            @RequestParam(value = "limit", defaultValue = "10")int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getAllVisits(token, offset, limit);
    }

}
