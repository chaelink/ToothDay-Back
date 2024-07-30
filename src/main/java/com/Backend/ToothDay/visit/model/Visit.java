package com.Backend.ToothDay.visit.model;

import com.Backend.ToothDay.jwt.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference; // 추가
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private long id;

    @Column(name = "visit_date")
    private String visitDate;

    @Column(name = "is_shared")
    @JsonProperty("shared")
    private boolean isShared = false;  // 기본 값 설정

    @CreationTimestamp
    private Timestamp createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // 순환 참조 방지
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private Dentist dentist;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 순환 참조 방지
    private List<Treatment> treatmentlist;

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", visitDate='" + visitDate + '\'' +
                ", isShared=" + isShared +
                ", createDate=" + createDate +
                '}';
    }

}
