package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mypage")
public class MyPageController {
    @Autowired
    private MyPageService myPageService;

    @GetMapping("/visit")
    public List<VisitRecordDTO> getVisitRecords(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return myPageService.getVisitRecordsForUser(token, offset, limit);
    }

    @GetMapping("/visit/{visitId}")
    public VisitRecordDTO getVisitRecordDetails(@PathVariable Long visitId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return myPageService.getVisitRecordById(visitId, token);
    }

    @GetMapping("/treatment")
    public Map<Integer, List<VisitRecordDTO>> getVisitRecordsGroupedByToothId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return myPageService.getVisitRecordsGroupedByToothId(token);
    }
}
