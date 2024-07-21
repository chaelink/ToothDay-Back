package com.Backend.ToothDay.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitListDTO {
    private Long dentistId;
    private String dentistName;
    private String dentistAddress;
    private String visitDate;
    private boolean isShared;
    private List<TreatmentDTO> treatmentList;
    private Long totalAmount;
    private boolean writtenByCurrentUser;
    private Long visitId;
    private Long userId;
    private String profileImageUrl;
}
