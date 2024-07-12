package com.Backend.ToothDay.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DentistDTO {

    private Long dentistId; // MySQL에서는 주로 Long 타입을 사용하는 것이 일반적입니다.
    private String dentistName;
    private String dentistAddress;

}
