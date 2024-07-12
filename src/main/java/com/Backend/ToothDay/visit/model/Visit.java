package com.Backend.ToothDay.visit.model;


import com.Backend.ToothDay.jwt.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private long id;

    // Removed redundant fields
    // private int dentistId;
    // private String dentistName;
    // private String dentistAddress;

    @Temporal(TemporalType.DATE)
    private Date visitDate;

    @Column(name = "is_shared")
    @JsonProperty("shared")
    private boolean isShared = false;  // 기본 값 설정

    @CreationTimestamp
    private Timestamp createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private Dentist dentist;

//    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Treatment> treatmentlist;
}
