package com.Backend.ToothDay.visit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Treatment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Long treatmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tooth_id", referencedColumnName = "tooth_id")
    private ToothNumber toothNumber;

    @Enumerated(EnumType.STRING)
    private Category category;
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitId")
    @JsonBackReference
    private Visit visit;
}
