package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.visit.dto.TreatmentDTO;
import com.Backend.ToothDay.visit.dto.VisitListDTO;
import com.Backend.ToothDay.visit.model.Treatment;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VisitListService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VisitRepository visitRepository;

    @Transactional
    public List<VisitListDTO> getAllVisitRecords(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        // 현재 사용자의 방문 기록 조회 (가장 최근 1개만)
        List<Visit> userVisits = visitRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Visit::getVisitDate).reversed()) // 방문 날짜 기준으로 내림차순 정렬
                .limit(1) // 최근 1개만
                .collect(Collectors.toList());

        // 현재 사용자가 아닌 사용자 중 isShared가 true인 방문 기록 조회
        List<Visit> allSharedVisits = visitRepository.findByIsShared(true).stream()
                .filter(visit -> visit.getUser() != null && !Objects.equals(visit.getUser().getId(), userId)) // 현재 사용자의 방문 기록은 제외
                .sorted(Comparator.comparing(Visit::getVisitDate).reversed()) // 방문 날짜 기준으로 내림차순 정렬
                .limit(3) // 최근 3개만
                .collect(Collectors.toList());

        // 모든 방문 기록을 하나의 리스트로 합치기
        List<VisitListDTO> userVisitDTOs = userVisits.stream()
                .map(visit -> convertToVisitListDTO(visit, userId, true)) // 현재 사용자의 기록은 true
                .collect(Collectors.toList());

        List<VisitListDTO> sharedVisitDTOs = allSharedVisits.stream()
                .map(visit -> convertToVisitListDTO(visit, userId, false)) // 공유된 기록은 false
                .collect(Collectors.toList());

        // 사용자 방문 기록 뒤에 공유된 기록이 나오도록 조합
        userVisitDTOs.addAll(sharedVisitDTOs);
        return userVisitDTOs;
    }

    private VisitListDTO convertToVisitListDTO(Visit visit, Long currentUserId, boolean writtenByCurrentUser) {
        String dentistName = (visit.getDentist() != null) ? visit.getDentist().getDentistName() : "정보 없음";
        String dentistAddress = (visit.getDentist() != null) ? visit.getDentist().getDentistAddress() : "정보 없음";

        // Treatment 리스트 변환
        List<TreatmentDTO> treatmentDTOList = visit.getTreatmentlist() != null
                ? visit.getTreatmentlist().stream().map(this::convertToTreatmentDTO).collect(Collectors.toList())
                : List.of();

        // totalAmount 계산
        long totalAmount = visit.getTreatmentlist() != null
                ? visit.getTreatmentlist().stream().mapToInt(Treatment::getAmount).sum()
                : 0;

        Long userId = (visit.getUser() != null) ? visit.getUser().getId() : null;

        // `isShared` 값 설정: 현재 사용자의 경우 false, 그렇지 않으면 원래 값 사용
        return VisitListDTO.builder()
                .visitDate(visit.getVisitDate() != null ? visit.getVisitDate().toString() : "정보 없음")
                .dentistName(dentistName)
                .dentistAddress(dentistAddress)
                .treatmentList(treatmentDTOList)
                .visitID(visit.getId())
                .userID(userId)
                .isShared(currentUserId.equals(userId) ? false : visit.isShared()) // 수정된 부분
                .totalAmount(totalAmount)
                .writtenByCurrentUser(writtenByCurrentUser) // 추가된 부분
                .build();
    }

    private TreatmentDTO convertToTreatmentDTO(Treatment treatment) {
        return TreatmentDTO.builder()
                .toothId(treatment.getToothNumber() != null ? treatment.getToothNumber().getToothid() : null)
                .category(treatment.getCategory().name())
                .amount(treatment.getAmount())
                .build();
    }
}
