package com.Backend.ToothDay.visit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "dentist")
    @JsonIgnore
    private List<Visit> visits;  // visits 필드를 직렬화하지 않도록 설정
}
