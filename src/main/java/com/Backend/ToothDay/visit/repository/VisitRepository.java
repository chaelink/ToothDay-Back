package com.Backend.ToothDay.visit.repository;

import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    @Query("SELECT v FROM Visit v WHERE v.user.id = :userId ORDER BY v.visitDate DESC")
    List<Visit> findByUserId(@Param("userId") Long userId, Pageable pageable);
    List<Visit> findByUserId(Long userId);
    List<Visit> findByIsShared(Boolean isShared);

    void deleteAllByUserId(Long userId);
}
