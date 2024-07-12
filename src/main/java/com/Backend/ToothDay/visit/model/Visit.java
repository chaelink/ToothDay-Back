package com.Backend.ToothDay.visit.model;


import com.Backend.ToothDay.jwt.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

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

    private boolean isShared;

    @CreationTimestamp
    private Timestamp createDate;

    private Long dentistId;
    private String dentistName;
    private String dentistAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
