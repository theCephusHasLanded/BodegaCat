package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.BodegaVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodegaVisitRepository extends JpaRepository<BodegaVisit, Long> {
    List<BodegaVisit> findByUserId(Long userId);
    List<BodegaVisit> findByBodegaStoreId(Long bodegaStoreId);
    long countByUserId(Long userId);
}