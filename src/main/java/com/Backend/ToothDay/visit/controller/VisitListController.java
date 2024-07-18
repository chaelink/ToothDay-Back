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
    public List<VisitListDTO> getAllVisitRecords(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getAllVisitRecords(token);
    }

    @GetMapping("/categories")
    public List<VisitListDTO> getVisitsByCategories(
            @RequestParam List<String> categories,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getVisitsByCategories(categories, token);
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
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getVisitsByCategory(category, token);
    }
    @GetMapping("/category/전체")
    public List<VisitListDTO> getAllVisits(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return visitListService.getAllVisits(token);
    }

}
