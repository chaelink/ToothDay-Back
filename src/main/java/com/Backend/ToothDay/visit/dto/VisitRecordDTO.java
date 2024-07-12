package com.Backend.ToothDay.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitRecordDTO {
    private Long userId;
    private Integer dentistId;
    private Date visitDate;
    private boolean isShared;
//    private List<TreatmentDTO> treatmentlist;

    public boolean getIsShared() {
        return isShared;
    }
}