package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository; // UserRepository 추가
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitListService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private UserRepository userRepository; // UserRepository 추가

    // 진료 목록 초기 화면
    @Transactional
    public List<VisitListDTO> getAllVisitRecords(String token, int offset, int limit) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        List<Visit> userVisits = visitRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Visit::getId).reversed())
                .limit(1)
                .collect(Collectors.toList());

        List<Visit> allSharedVisits = visitRepository.findByIsShared(true).stream()
                .filter(visit -> visit.getUser() != null && !Objects.equals(visit.getUser().getId(), userId))
                .sorted(Comparator.comparing(Visit::getId).reversed())
                //.limit(3)
                .collect(Collectors.toList());

        List<VisitListDTO> userVisitDTOs = convertToVisitListDTOs(userVisits, userId, true);
        List<VisitListDTO> sharedVisitDTOs = convertToVisitListDTOs(allSharedVisits, userId, false);

        userVisitDTOs.addAll(sharedVisitDTOs);
        int fromIndex = offset;
        int toIndex = Math.min(offset + limit, userVisitDTOs.size());

        return userVisitDTOs.subList(fromIndex, toIndex);
    }

    // 카테고리별 진료 목록
    @Transactional
    public List<VisitListDTO> getVisitsByCategories(List<String> categories, String token, int offset, int limit) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        // 현재 사용자가 아닌 사용자 중 isShared가 true인 방문 기록 조회
        List<Visit> allSharedVisits = visitRepository.findByIsShared(true).stream()
                .filter(visit -> visit.getUser() != null && !Objects.equals(visit.getUser().getId(), userId))
                .filter(visit -> visitHasCategories(visit, categories))
                .sorted(Comparator.comparing(Visit::getId).reversed()) // visitId 기준으로 내림차순 정렬
                .collect(Collectors.toList());

        // 공유된 방문 기록 DTO로 변환
        List<VisitListDTO> sharedVisitDTOs = convertToVisitListDTOs(allSharedVisits, userId, false);

        int fromIndex = offset;
        int toIndex = Math.min(offset + limit, sharedVisitDTOs.size());
        return sharedVisitDTOs.subList(fromIndex, toIndex);
    }

    // 특정 카테고리의 진료 목록
    @Transactional
    public List<VisitListDTO> getVisitsByCategory(String category, String token, int offset, int limit) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        // 현재 사용자가 아닌 사용자 중 isShared가 true인 방문 기록 조회
        List<Visit> allSharedVisits = visitRepository.findByIsShared(true).stream()
                .filter(visit -> visit.getUser() != null && !Objects.equals(visit.getUser().getId(), userId))
                .filter(visit -> visitHasCategory(visit, category))
                .sorted(Comparator.comparing(Visit::getId).reversed()) // visitId 기준으로 내림차순 정렬
                .collect(Collectors.toList());

        // 공유된 방문 기록 DTO로 변환
        List<VisitListDTO> sharedVisitDTOs = convertToVisitListDTOs(allSharedVisits, userId, false);

        int fromIndex = offset;
        int toIndex = Math.min(offset + limit, sharedVisitDTOs.size());
        return sharedVisitDTOs.subList(fromIndex, toIndex);
    }

    @Transactional
    public List<VisitListDTO> getAllVisits(String token, int offset, int limit) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        // isShared가 true인 모든 방문 기록 조회
        List<Visit> allSharedVisits = visitRepository.findByIsShared(true).stream()
                .filter(visit -> visit.getUser() != null && !Objects.equals(visit.getUser().getId(), userId))
                .sorted(Comparator.comparing(Visit::getId).reversed()) // visitId 기준으로 내림차순 정렬
                .collect(Collectors.toList());

        // 공유된 방문 기록 DTO로 변환
        List<VisitListDTO> sharedVisitDTOs =convertToVisitListDTOs(allSharedVisits, userId, false);
        int fromIndex = offset;
        int toIndex = Math.min(offset + limit, sharedVisitDTOs.size());
        return sharedVisitDTOs.subList(fromIndex, toIndex);
    }

    @Transactional
    public VisitListDTO getVisitById(Long visitId, String token) {
        // 토큰에서 유저 아이디 가져오기 (권한 확인 용도)
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 특정 visitId로 방문 기록 조회
        Optional<Visit> optionalVisit = visitRepository.findById(visitId);

        // 방문 기록이 존재하지 않으면 예외 발생
        Visit visit = optionalVisit
                .orElseThrow(() -> new RuntimeException("방문 기록을 찾을 수 없습니다."));

        // 현재 사용자가 이 방문 기록을 볼 수 있는지 확인
        boolean isAuthorizedToView = visit.isShared() || (visit.getUser() != null && Objects.equals(visit.getUser().getId(), userId));
        if (!isAuthorizedToView) {
            throw new RuntimeException("이 방문 기록을 볼 권한이 없습니다.");
        }

        // Visit을 DTO로 변환
        return convertToVisitListDTO(visit, userId, visit.getUser() != null && Objects.equals(visit.getUser().getId(), userId));
    }

    private boolean visitHasCategories(Visit visit, List<String> categories) {
        return visit.getTreatmentlist() != null && visit.getTreatmentlist().stream()
                .anyMatch(treatment -> categories.contains(treatment.getCategory().name()));
    }

    private boolean visitHasCategory(Visit visit, String category) {
        return visit.getTreatmentlist() != null && visit.getTreatmentlist().stream()
                .anyMatch(treatment -> category.contains(treatment.getCategory().name())); // 수정됨
    }

    private List<VisitListDTO> convertToVisitListDTOs(List<Visit> visits, Long currentUserId, boolean writtenByCurrentUser) {
        return visits.stream()
                .map(visit -> convertToVisitListDTO(visit, currentUserId, writtenByCurrentUser))
                .collect(Collectors.toList());
    }

    private VisitListDTO convertToVisitListDTO(Visit visit, Long currentUserId, boolean writtenByCurrentUser) {
        String dentistName = (visit.getDentist() != null) ? visit.getDentist().getDentistName() : "정보 없음";
        String dentistAddress = (visit.getDentist() != null) ? visit.getDentist().getDentistAddress() : "정보 없음";

        List<TreatmentDTO> treatmentDTOList = visit.getTreatmentlist() != null
                ? visit.getTreatmentlist().stream().map(this::convertToTreatmentDTO).collect(Collectors.toList())
                : List.of();

        long totalAmount = visit.getTreatmentlist() != null
                ? visit.getTreatmentlist().stream().mapToInt(Treatment::getAmount).sum()
                : 0;

        Long userId = (visit.getUser() != null) ? visit.getUser().getId() : null;
        String profileImageUrl = (visit.getUser() != null) ? visit.getUser().getProfileImageUrl() : "정보 없음"; // 추가된 부분

        return VisitListDTO.builder()
                .visitDate(visit.getVisitDate() != null ? visit.getVisitDate().toString() : "정보 없음")
                .dentistId(visit.getDentist() != null ? visit.getDentist().getDentistId() : null)
                .dentistName(dentistName)
                .dentistAddress(dentistAddress)
                .treatmentList(treatmentDTOList)
                .visitId(visit.getId())
                .userId(userId)
                .isShared(visit.isShared())
                .totalAmount(totalAmount)
                .writtenByCurrentUser(writtenByCurrentUser)
                .profileImageUrl(profileImageUrl) // 추가된 부분
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