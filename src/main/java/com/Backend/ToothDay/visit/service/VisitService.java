package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.Backend.ToothDay.visit.dto.TreatmentDTO;
import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Dentist;
import com.Backend.ToothDay.visit.model.ToothNumber;
import com.Backend.ToothDay.visit.model.Treatment;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.repository.DentistRepository;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitService {

    private static final Logger logger = LoggerFactory.getLogger(VisitService.class);

    @Autowired
    private VisitRepository visitRecordRepository;

    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Visit createVisitRecord(VisitRecordDTO visitRecordDTO) {
        logger.info("DTO Content: {}", visitRecordDTO);
        User user = userRepository.findById(visitRecordDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Dentist dentist = dentistRepository.findById(visitRecordDTO.getDentistId())
                .orElseThrow(() -> new RuntimeException("Dentist not found"));

        Visit visit = Visit.builder()
                .user(user)
                .dentist(dentist)
                .visitDate(visitRecordDTO.getVisitDate())
                .isShared(visitRecordDTO.getIsShared())  // 명시적으로 설정
                .build();


        Visit savedVisit = visitRecordRepository.save(visit);
        return savedVisit;
    }
}
