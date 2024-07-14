package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.VisitListDTO;
import com.Backend.ToothDay.visit.service.VisitListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class VisitListController {

    @Autowired
    private VisitListService visitListService;

    @GetMapping("/visit")
    public List<VisitListDTO> getAllVisitRecords(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        return visitListService.getAllVisitRecords(token);
    }


}
