package com.Backend.ToothDay.visit.service;

import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.Backend.ToothDay.visit.dto.VisitListDTO;
import com.Backend.ToothDay.visit.model.Visit;
import com.Backend.ToothDay.visit.repository.DentistRepository;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitListService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DentistRepository dentistRepository;

    @Transactional
    public List<VisitListDTO> getAllVisitRecords(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("토큰에서 유저 아이디를 가져올 수 없습니다.");
        }

        List<Visit> visits = visitRepository.findByUserId(userId);
        return visits.stream().map(this::convertToVisitListDTO).collect(Collectors.toList());
    }



    private VisitListDTO convertToVisitListDTO(Visit visit) {
        String dentistName = (visit.getDentist() != null) ? visit.getDentist().getDentistName() : "정보 없음";
        String dentistAddress = (visit.getDentist() != null) ? visit.getDentist().getDentistAddress() : "정보 없음";
        String category = (visit.getTreatmentlist() != null && !visit.getTreatmentlist().isEmpty()) ? visit.getTreatmentlist().get(0).getCategory().name() : "정보 없음";
        String amount = (visit.getTreatmentlist() != null && !visit.getTreatmentlist().isEmpty()) ? visit.getTreatmentlist().get(0).getAmount().toString() : "정보 없음";

        return new VisitListDTO(
                visit.getVisitDate() != null ? visit.getVisitDate().toString() : "정보 없음",
                dentistName,
                dentistAddress,
                category,
                amount,
                visit.getId()
        );
    }
}
