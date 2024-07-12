package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisitController {

    @Autowired
    private VisitService visitService;

    @PostMapping("/visit")
    public ResponseEntity<Visit> createVisitRecord(@RequestBody VisitRecordDTO visitRecordDTO) {
        Visit visit = visitService.createVisitRecord(visitRecordDTO);
        return ResponseEntity.ok(visit);
    }
}
