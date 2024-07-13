package com.Backend.ToothDay.visit.repository;

import com.Backend.ToothDay.visit.model.Treatment;
import com.Backend.ToothDay.visit.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    @Transactional
    void deleteByVisit(Visit visit);
}
