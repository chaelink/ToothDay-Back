package com.Backend.ToothDay.visit.repository;

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

    // 페이징 처리와 내림차순 정렬
    @Query("SELECT v FROM Visit v WHERE v.user.id = :userId ORDER BY v.visitDate DESC")
    Page<Visit> findByUserIdOrderByVisitDateDesc(@Param("userId") Long userId, Pageable pageable);    // 페이징 처리와 오름차순 정렬
    @Query("SELECT v FROM Visit v WHERE v.user.id = :userId ORDER BY v.visitDate ASC")
    Page<Visit> findByUserIdOrderByVisitDateAsc(@Param("userId") Long userId, Pageable pageable);

    List<Visit> findByUserId(Long userId);

    List<Visit> findByIsShared(Boolean isShared);

    @Query("SELECT v FROM Visit v WHERE v.user.id = :userId ORDER BY v.visitDate ASC")
    List<Visit> findByUserIdOrderByVisitDateAsc(@Param("userId") Long userId);

    void deleteAllByUserId(Long userId);
}