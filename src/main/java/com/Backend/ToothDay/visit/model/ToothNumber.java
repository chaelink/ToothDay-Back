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
@Table(name = "toothnumber") // 테이블 이름 지정
public class ToothNumber {
    @Id
    @Column(name = "tooth_id") // 컬럼 이름 지정
    private Long toothid;

    @Column(name = "tooth_name") // 컬럼 이름 지정
    private String toothName;

    @OneToMany(mappedBy = "toothNumber")  // 여기서 mappedBy 속성을 수정
    @JsonIgnore
    private List<Treatment> treatments;  // Treatment 필드를 직렬화하지 않도록 설정
}
