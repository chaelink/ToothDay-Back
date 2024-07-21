package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.Backend.ToothDay.visit.dto.TreatmentDTO;
import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.*;
//import com.Backend.ToothDay.visit.model.Treatment;
import com.Backend.ToothDay.visit.repository.DentistRepository;
import com.Backend.ToothDay.visit.repository.ToothNumberRepository;
import com.Backend.ToothDay.visit.repository.TreatmentRepository;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private static final Logger logger = LoggerFactory.getLogger(VisitService.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private ToothNumberRepository toothNumberRepository;


    @Transactional
    public VisitRecordDTO createVisitRecord(VisitRecordDTO visitRecordDTO, String token) {
        logger.info("DTO Content: {}", visitRecordDTO);

        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Dentist dentist = dentistRepository.findById(Long.valueOf(visitRecordDTO.getDentistId()))
                .orElseThrow(() -> new RuntimeException("치과를 찾을 수 없습니다."));

        Visit visit = Visit.builder()
                .user(user)
                .dentist(dentist)
                .visitDate(visitRecordDTO.getVisitDate())
                .isShared(visitRecordDTO.getIsShared())
                .build();

        Visit savedVisit = visitRepository.save(visit);

        List<Treatment> treatments = visitRecordDTO.getTreatmentList().stream().map(dto -> {
            ToothNumber toothNumber = null;
            if (!requiresNoToothNumber(dto.getCategory())) {
                toothNumber = toothNumberRepository.findById(dto.getToothId())
                        .orElseThrow(() -> new RuntimeException("치아 번호를 찾을 수 없습니다."));
            }

            return Treatment.builder()
                    .category(Category.valueOf(dto.getCategory()))
                    .amount(dto.getAmount())
                    .toothNumber(toothNumber)
                    .visit(savedVisit)
                    .build();
        }).collect(Collectors.toList());

        treatmentRepository.saveAll(treatments);
        savedVisit.setTreatmentlist(treatments);

        //totalAmount 설정
        int totalAmount = treatments.stream()
                .mapToInt(Treatment::getAmount)
                .sum();

        // VisitRecordDTO 객체 업데이트
        return VisitRecordDTO.builder()
                .visitId(savedVisit.getId()) // visitId 설정
                .dentistId(dentist.getDentistId())
                .dentistName(dentist.getDentistName())
                .dentistAddress(dentist.getDentistAddress())
                .visitDate(savedVisit.getVisitDate())
                .isShared(savedVisit.isShared())
                .treatmentList(mapTreatmentListToTreatmentDTOList(treatments))
                .totalAmount(totalAmount)
                .isWrittenByCurrentUser(userId.equals(savedVisit.getUser().getId()))
                .build();
    }

    private boolean requiresNoToothNumber(String category) {
        return "잇몸".equals(category) || "스케일링".equals(category);
    }

    @Transactional
    public void deleteVisitRecord(Long visitId, String token) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("진료 기록이 없습니다."));
        Long userId = jwtUtil.getUserIdFromToken(token);
        System.out.println("userId = " + userId);

        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        Long visitUserId = visit.getUser().getId();

        if (visitUserId == null || !visitUserId.equals(userId)) {
            throw new RuntimeException("본인만 게시글 삭제가 가능합니다.");
        }
        visitRepository.delete(visit);
    }

    @Transactional
    public VisitRecordDTO updateVisitRecord(Long visitId, VisitRecordDTO visitRecordDTO, String token) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("진료 기록이 없습니다."));

        Long userId = jwtUtil.getUserIdFromToken(token);

        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        Long visitUserId = visit.getUser().getId();

        if (visitUserId == null || !visitUserId.equals(userId)) {
            throw new RuntimeException("본인만 게시글 수정이 가능합니다.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Dentist dentist = dentistRepository.findById(Long.valueOf(visitRecordDTO.getDentistId()))
                .orElseThrow(() -> new RuntimeException("치과를 찾을 수없습니다."));

        visit.setUser(user);
        visit.setDentist(dentist);
        visit.setVisitDate(visitRecordDTO.getVisitDate());
        visit.setShared(visitRecordDTO.getIsShared());

        // Treatments 업데이트
        List<Treatment> updatedTreatments = new ArrayList<>();

        for (TreatmentDTO dto : visitRecordDTO.getTreatmentList()) {
            Treatment treatment = new Treatment();
            treatment.setVisit(visit);
            treatment.setCategory(Category.valueOf(dto.getCategory()));
            treatment.setAmount(dto.getAmount());

            if (!requiresNoToothNumber(dto.getCategory())) {
                if (dto.getToothId() == null) {
                    throw new RuntimeException("치아 번호는 필수입니다.");
                }
                ToothNumber toothNumber = toothNumberRepository.findById(dto.getToothId())
                        .orElseThrow(() -> new RuntimeException("치아 번호를 찾을 수 없습니다."));
                treatment.setToothNumber(toothNumber);
            }

            updatedTreatments.add(treatment);
        }

        // 기존 Treatment 삭제 후 새로운 Treatments 저장
        visit.getTreatmentlist().clear(); // 기존 Treatments 모두 제거
        visit.getTreatmentlist().addAll(updatedTreatments);

        // isWrittenByCurrentUser 값을 설정
        boolean isWrittenByCurrentUser =  visit.getUser() != null && visit.getUser().getId() == userId;
        visitRecordDTO.setWrittenByCurrentUser(isWrittenByCurrentUser);

        // Visit를 저장하고 변환된 VisitRecordDTO를 반환
        visitRepository.save(visit);
        VisitRecordDTO updatedVisitRecordDTO = mapVisitToVisitRecordDTO(visit);

        // visitId 설정
        updatedVisitRecordDTO.setVisitId(visitId);

        return updatedVisitRecordDTO;

    }

    // Visit 객체를 VisitRecordDTO로 변환하는 메서드
    public VisitRecordDTO mapVisitToVisitRecordDTO(Visit visit) {
        List<TreatmentDTO> treatmentDTOs = mapTreatmentListToTreatmentDTOList(visit.getTreatmentlist());

        //totalAmount 계산
        int totalAmount = treatmentDTOs.stream()
                .mapToInt(TreatmentDTO::getAmount)
                .sum();

        VisitRecordDTO visitRecordDTO = VisitRecordDTO.builder()
                .visitId(visit.getId())  // visitId 설정
                .dentistId(visit.getDentist().getDentistId())
                .dentistName(visit.getDentist().getDentistName())
                .dentistAddress(visit.getDentist().getDentistAddress())
                .visitDate(visit.getVisitDate())
                .isShared(visit.isShared())
                .treatmentList(mapTreatmentListToTreatmentDTOList(visit.getTreatmentlist()))
                .totalAmount(totalAmount)
                .isWrittenByCurrentUser(false) // 기본값으로 false 설정
                .build();

        // isWrittenByCurrentUser 값을 설정
        if (visit.getUser() != null) {
            Long userId = visit.getUser().getId();
            visitRecordDTO.setWrittenByCurrentUser(userId != null && userId.equals(visit.getUser().getId()));
        }

        return visitRecordDTO;
    }
    private List<TreatmentDTO> mapTreatmentListToTreatmentDTOList(List<Treatment> treatments) {
        return treatments.stream()
                .map(treatment -> TreatmentDTO.builder()
                        .toothId(treatment.getToothNumber() != null ? treatment.getToothNumber().getToothid() : null)
                        .category(treatment.getCategory().toString())
                        .amount(treatment.getAmount())
                        .build())
                .collect(Collectors.toList());
    }
}


