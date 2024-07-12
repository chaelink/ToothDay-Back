package com.Backend.ToothDay.visit.controller;

import com.Backend.ToothDay.visit.dto.DentistDTO;
import com.Backend.ToothDay.visit.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dentists")
public class DentistController {

    private final DentistService dentistService;

    @Autowired
    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping("/search")
    public List<DentistDTO> searchDentists(@RequestParam String query) {
        return dentistService.searchDentists(query);
    }

}
