package com.Backend.ToothDay.visit.repository;

import com.Backend.ToothDay.visit.model.ToothNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

public interface ToothNumberRepository extends JpaRepository<ToothNumber, Long> {

}
