package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.visit.dto.TreatmentDTO;
import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.repository.ReVisitRepository;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private ReVisitRepository reVisitRepository;

    public List<VisitRecordDTO> getVisitRecordsForUser(String token, int offset, int limit) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        List<Visit> userVisits = reVisitRepository.findByUserIdOrderByVisitDateDesc(userId, offset, limit);

        return userVisits.stream()
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

        boolean isWrittenByCurrentUser = visit.getUser() != null && visit.getUser().getId()==(userId);

        return VisitRecordDTO.builder()
                .visitId(visit.getId())
                .dentistId(visit.getDentist() != null ? visit.getDentist().getDentistId() : null)
                .dentistName(visit.getDentist() != null ? visit.getDentist().getDentistName() : null)
                .dentistAddress(visit.getDentist() != null ? visit.getDentist().getDentistAddress() : null)
                .visitDate(visit.getVisitDate())
                .isShared(visit.isShared())
                .treatmentList(treatmentDTOs)
                .totalAmount(totalAmount)
                .isWrittenByCurrentUser(isWrittenByCurrentUser)
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

    // 새로운 서비스 로직 수정
    public Map<Integer, List<VisitRecordDTO>> getVisitRecordsGroupedByToothId(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<Visit> visits = visitRepository.findByUserIdOrderByVisitDateAsc(userId); // 방문 기록을 visitDate 기준으로 정렬

        // 데이터를 toothId별로 그룹핑
        Map<Integer, Map<Long, VisitRecordDTO>> groupedByToothId = new HashMap<>();

        for (Visit visit : visits) {
            visit.getTreatmentlist().forEach(treatment -> {
                // toothId가 null이거나 0인 경우 처리하지 않음
                if (treatment.getToothNumber() == null || treatment.getToothNumber().getToothid() == 0) {
                    return;
                }
                int toothId = Math.toIntExact(treatment.getToothNumber().getToothid());
                long visitId = visit.getId();

                // toothId 그룹을 얻거나 생성
                Map<Long, VisitRecordDTO> visitMap = groupedByToothId.computeIfAbsent(toothId, k -> new HashMap<>());

                // visitId 그룹을 얻거나 생성
                VisitRecordDTO visitRecordDTO = visitMap.get(visitId);
                if (visitRecordDTO == null) {
                    visitRecordDTO = VisitRecordDTO.builder()
                            .visitId(visit.getId())
                            .dentistId(visit.getDentist() != null ? visit.getDentist().getDentistId() : null)
                            .dentistName(visit.getDentist() != null ? visit.getDentist().getDentistName() : null)
                            .dentistAddress(visit.getDentist() != null ? visit.getDentist().getDentistAddress() : null)
                            .visitDate(visit.getVisitDate())
                            .isShared(visit.isShared())
                            .treatmentList(new ArrayList<>())
                            .totalAmount(0)
                            .isWrittenByCurrentUser(visit.getUser() != null && userId != null && visit.getUser().getId() == userId)
                            .build();

                    visitMap.put(visitId, visitRecordDTO);
                }

                // 치료 내역 추가
                visitRecordDTO.getTreatmentList().add(new TreatmentDTO(
                        (long) toothId,
                        treatment.getCategory() != null ? treatment.getCategory().name() : null,
                        treatment.getAmount() != null ? treatment.getAmount() : 0
                ));

                // 총 금액 업데이트
                visitRecordDTO.setTotalAmount(visitRecordDTO.getTotalAmount() + (treatment.getAmount() != null ? treatment.getAmount() : 0));
            });
        }

        // 데이터를 반환 형식에 맞게 변환
        return groupedByToothId.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new ArrayList<>(entry.getValue().values())
                ));
    }
}