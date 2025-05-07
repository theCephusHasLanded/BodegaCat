package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.BodegaVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BodegaVisitRepository extends JpaRepository<BodegaVisit, Long> {
    List<BodegaVisit> findByUserId(Long userId);
    
    List<BodegaVisit> findByBodegaStoreId(Long bodegaStoreId);
    
    @Query("SELECT COUNT(v) FROM BodegaVisit v WHERE v.user.id = ?1 AND v.bodegaStore.id = ?2")
    int countVisitsByUserAndBodega(Long userId, Long bodegaStoreId);
    
    @Query("SELECT v FROM BodegaVisit v WHERE v.visitDate BETWEEN ?1 AND ?2")
    List<BodegaVisit> findVisitsInPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT v.bodegaStore.id, COUNT(v) FROM BodegaVisit v GROUP BY v.bodegaStore.id ORDER BY COUNT(v) DESC")
    List<Object[]> findMostVisitedBodegas();
}