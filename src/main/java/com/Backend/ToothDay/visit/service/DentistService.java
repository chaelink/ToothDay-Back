package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.visit.dto.DentistDTO;
import com.Backend.ToothDay.visit.model.Dentist;
import com.Backend.ToothDay.visit.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistService {
    @Autowired
    private DentistRepository dentistRepository;

    public List<DentistDTO> searchDentists(String query) {
        List<Dentist> dentists = dentistRepository.findByDentistNameContaining(query);
        return dentists.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DentistDTO convertToDTO(Dentist dentist) {
        DentistDTO dto = new DentistDTO();
        dto.setDentistId(Long.valueOf(dentist.getDentistId()));
        dto.setDentistName(dentist.getDentistName());
        dto.setDentistAddress(dentist.getDentistAddress());
        return dto;
    }
}