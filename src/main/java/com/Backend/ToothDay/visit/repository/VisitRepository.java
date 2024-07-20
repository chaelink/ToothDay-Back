package com.Backend.ToothDay.visit.repository;

import com.Backend.ToothDay.visit.dto.VisitRecordDTO;
import com.Backend.ToothDay.visit.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByUserId(Long userId);
    List<Visit> findByIsShared(Boolean isShared);

    void deleteAllByUserId(Long userId);
}
