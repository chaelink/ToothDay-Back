package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.Backend.ToothDay.visit.dto.DentistDTO;
import com.Backend.ToothDay.visit.model.Dentist;
import com.Backend.ToothDay.visit.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistService {

    private final DentistRepository dentistRepository;

    @Autowired
    public DentistService(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    public List<DentistDTO> getAllDentists() {
        List<Dentist> dentists = dentistRepository.findAll();
        return dentists.stream()
                .map(DentistDTO::from)
                .collect(Collectors.toList());
    }

    public List<DentistDTO> searchDentists(String query) {
        List<Dentist> dentists = dentistRepository.findByDentistNameContainingIgnoreCase(query);
        return dentists.stream()
                .map(DentistDTO::from)
                .collect(Collectors.toList());
    }
}