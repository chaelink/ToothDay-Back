package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.visit.dto.TreatmentDTO;
import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VisitRepository visitRepository;
    public List<VisitRecordDTO> getVisitRecordsForUser(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<Visit> visits = visitRepository.findByUserId(userId);  // 사용자 ID로 방문 기록을 가져옴

        return visits.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    private VisitRecordDTO convertToDto(Visit visit) {
        List<TreatmentDTO> treatmentDTOs = visit.getTreatmentlist().stream()
                .map(treatment -> new TreatmentDTO(
                        treatment.getToothNumber() != null ? treatment.getToothNumber().getToothid() : null,
                        treatment.getCategory() != null ? treatment.getCategory().name() : null,
                        treatment.getAmount() != null ? treatment.getAmount() : 0))
                .collect(Collectors.toList());

        int totalAmount = treatmentDTOs.stream()
                .mapToInt(TreatmentDTO::getAmount)
                .sum();

        return VisitRecordDTO.builder()
                .dentistId(visit.getDentist() != null ? visit.getDentist().getDentistId() : null)
                .dentistName(visit.getDentist() != null ? visit.getDentist().getDentistName() : null)
                .visitDate(visit.getVisitDate())
                .isShared(visit.isShared())
                .treatmentlist(treatmentDTOs)
                .totalAmount(totalAmount)
                .build();
    }


    public VisitRecordDTO getVisitRecordById(Long visitId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("진료기록을 찾을 수 없습니다."));


        Long visitUserId = visit.getUser().getId();
        // 사용자가 자신의 기록만 조회할 수 있도록 검증
        if (visitUserId == null || !visitUserId.equals(userId)) {
            throw new RuntimeException("접근이 거부되었습니다. 본인의 진료 기록만 조회할 수 있습니다.");
        }

        return convertToDto(visit);
    }
}
