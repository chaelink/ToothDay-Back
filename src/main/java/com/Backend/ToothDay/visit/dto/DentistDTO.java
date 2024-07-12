package com.Backend.ToothDay.visit.dto;

import com.Backend.ToothDay.jwt.dto.UserDTO;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.visit.model.Dentist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DentistDTO {

    private Long id; // MySQL에서는 주로 Long 타입을 사용하는 것이 일반적입니다.
    private String dentistName;
    private String dentistAddress;

    public static DentistDTO from(Dentist dentist) {
        return DentistDTO.builder()
                .id(dentist.getId())
                .dentistName(dentist.getDentistName())
                .dentistAddress(dentist.getDentistAddress())
                .build();
    }
}
