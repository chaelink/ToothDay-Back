package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/mypage")
public class MyPageController {
    @Autowired
    private MyPageService myPageService;

    @GetMapping("/visit")
    public List<VisitRecordDTO> getVisitRecords(HttpServletRequest request){
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return myPageService.getVisitRecordsForUser(token);
    }
    @GetMapping("/visit/{visitId}")
    public VisitRecordDTO getVisitRecordDetails(@PathVariable Long visitId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return myPageService.getVisitRecordById(visitId, token);
    }
}
