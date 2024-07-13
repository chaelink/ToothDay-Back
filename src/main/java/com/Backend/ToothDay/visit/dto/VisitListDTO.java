package com.Backend.ToothDay.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitListDTO {
    private String date;
    private String dentistName;
    private String dentistAddress;
    private String category;
    private String amount;
    private Long visitID;
}
