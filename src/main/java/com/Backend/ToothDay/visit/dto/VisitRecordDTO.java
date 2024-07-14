package com.Backend.ToothDay.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitRecordDTO {
//    private Long userId;
    private Long dentistId;
    private String dentistName;  // 치과 이름 필드 추가
    private Date visitDate;
    private boolean isShared;
    private List<TreatmentDTO> treatmentlist;
    private Integer totalAmount;
    private boolean isWrittenByCurrentUser; // 유저가 작성한 진료기록인지 T/F로 받을 수 있는 변수 추가

    public boolean getIsShared() {
        return isShared;
    }
}