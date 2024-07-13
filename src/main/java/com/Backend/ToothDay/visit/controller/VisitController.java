package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class VisitController {

    @Autowired
    private VisitService visitService;

    @PostMapping("/visit")
    public ResponseEntity<Visit> createVisitRecord(@RequestBody VisitRecordDTO visitRecordDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Visit visit = visitService.createVisitRecord(visitRecordDTO, token);
        return ResponseEntity.ok(visit);
    }

    @DeleteMapping("/visit/{visitId}")
    public ResponseEntity<String> deleteVisitRecord(@PathVariable Long visitId, HttpServletRequest request){
        visitService.deleteVisitRecord(visitId, request.getHeader("Authorization").replace("Bearer ", ""));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("방문 기록이 성공적으로 삭제되었습니다.");
    }

    @PutMapping("/visit/{visitId}")
    public ResponseEntity<Visit> updateVisitRecord(@PathVariable Long visitId,
                                                   @RequestBody VisitRecordDTO visitRecordDTO,
                                                   HttpServletRequest request){
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Visit updatedVisit = visitService.updateVisitRecord(visitId, visitRecordDTO, token);
        return ResponseEntity.ok(updatedVisit);
    }
}
