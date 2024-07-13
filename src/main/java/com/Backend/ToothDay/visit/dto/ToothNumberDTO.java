package com.Backend.ToothDay.visit.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToothNumberDTO {
    private Long toothId;
    private String toothName;
}
