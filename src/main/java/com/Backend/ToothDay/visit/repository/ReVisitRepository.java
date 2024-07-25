package com.Backend.ToothDay.visit.repository;

import com.Backend.ToothDay.visit.model.Visit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ReVisitRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Visit> findByUserIdOrderByVisitDateDesc(Long userId, int offset, int limit) {
        String queryStr = "SELECT v FROM Visit v WHERE v.user.id = :userId ORDER BY v.visitDate DESC";
        TypedQuery<Visit> query = em.createQuery(queryStr, Visit.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return query.getResultList();
    }

}
