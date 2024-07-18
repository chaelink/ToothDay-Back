package com.Backend.ToothDay.visit.repository;

import com.Backend.ToothDay.visit.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {
    List<Dentist> findByDentistNameContaining(String query);

}
