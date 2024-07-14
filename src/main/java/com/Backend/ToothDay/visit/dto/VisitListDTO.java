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
    private String visitDate;
    private String dentistName;
    private String dentistAddress;
    private List<TreatmentDTO> treatmentList;
    private Long visitID;
    private Long userID;
    private boolean isShared;
    private Long totalAmount;
    private boolean writtenByCurrentUser;
}
