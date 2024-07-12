package com.Backend.ToothDay.visit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dentist") // 테이블 이름 지정
public class Dentist {
    @Id
    @Column(name = "dentist_id") // 컬럼 이름 지정
    private Integer dentistId;

    @Column(name = "dentist_name") // 컬럼 이름 지정
    private String dentistName;

    @Column(name = "dentist_address") // 컬럼 이름 지정
    private String dentistAddress;

    // getters and setters
}
