package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.visit.dto.TreatmentDTO;
import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VisitRepository visitRepository;

    public List<VisitRecordDTO> getVisitRecordsForUser(String token, int offset, int limit) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(offset, limit);

        // 페이지 처리: offset과 limit을 사용하여 데이터 조회
        List<Visit> visits = visitRepository.findByUserId(userId, pageable);

        return visits.stream()
                .map(visit -> convertToDto(visit, userId))
                .collect(Collectors.toList());
    }

    private VisitRecordDTO convertToDto(Visit visit, Long userId) {
        List<TreatmentDTO> treatmentDTOs = visit.getTreatmentlist().stream()
                .map(treatment -> new TreatmentDTO(
                        treatment.getToothNumber() != null ? treatment.getToothNumber().getToothid() : null,
                        treatment.getCategory() != null ? treatment.getCategory().name() : null,
                        treatment.getAmount() != null ? treatment.getAmount() : 0))
                .collect(Collectors.toList());

        int totalAmount = treatmentDTOs.stream()
                .mapToInt(TreatmentDTO::getAmount)
                .sum();
        // 작성자 여부 설정
        boolean isWrittenByCurrentUser = visit.getUser() != null && visit.getUser().getId() == userId;

        return VisitRecordDTO.builder()
                .visitId(visit.getId()) // visitId 추가
                .dentistId(visit.getDentist() != null ? visit.getDentist().getDentistId() : null)
                .dentistName(visit.getDentist() != null ? visit.getDentist().getDentistName() : null)
                .dentistAddress(visit.getDentist() != null ? visit.getDentist().getDentistAddress() : null)
                .visitDate(visit.getVisitDate())
                .isShared(visit.isShared())
                .treatmentList(treatmentDTOs)
                .totalAmount(totalAmount)
                .isWrittenByCurrentUser(isWrittenByCurrentUser) // 작성자 여부 추가
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

        return convertToDto(visit, userId);
    }

    // 새로운 서비스 로직 추가
    public Map<Integer, List<VisitRecordDTO>> getVisitRecordsGroupedByToothId(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<Visit> visits = visitRepository.findByUserId(userId);  // 사용자 ID로 방문 기록을 가져옴

        return visits.stream()
                .flatMap(visit -> visit.getTreatmentlist().stream()
                        .map(treatment -> VisitRecordDTO.builder()
                                .visitId(visit.getId()) // visitId 추가
                                .dentistId(visit.getDentist() != null ? visit.getDentist().getDentistId() : null)
                                .dentistName(visit.getDentist() != null ? visit.getDentist().getDentistName() : null)
                                .dentistAddress(visit.getDentist() != null ? visit.getDentist().getDentistAddress() : null)
                                .visitDate(visit.getVisitDate())
                                .isShared(visit.isShared())
                                .treatmentList(List.of(new TreatmentDTO(
                                        treatment.getToothNumber() != null ? treatment.getToothNumber().getToothid() : null,
                                        treatment.getCategory() != null ? treatment.getCategory().name() : null,
                                        treatment.getAmount() != null ? treatment.getAmount() : 0
                                )))
                                .totalAmount(treatment.getAmount())
                                .isWrittenByCurrentUser(visit.getUser() != null && visit.getUser().getId() == (userId))
                                .build()))
                .filter(treatmentDTO -> treatmentDTO.getTreatmentList().get(0).getToothId() != null) // toothId가 null인 값을 제외함
                .collect(Collectors.groupingBy(treatmentDTO -> treatmentDTO.getTreatmentList().get(0).getToothId().intValue())); // 그룹화
    }
}