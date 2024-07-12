package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.DentistDTO;
import com.Backend.ToothDay.visit.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dentists")
public class DentistController {
    @Autowired
    private DentistService dentistService;

    @GetMapping("/search")
    public ResponseEntity<List<DentistDTO>> searchDentists(@RequestParam("query") String query) {
        List<DentistDTO> dentists = dentistService.searchDentists(query);
        return ResponseEntity.ok(dentists);
    }
}