package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Fundraiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundraiserRepository extends JpaRepository<Fundraiser, Long> {
    List<Fundraiser> findByBodegaStoreId(Long bodegaStoreId);
    
    List<Fundraiser> findByActiveAndBodegaStoreId(boolean active, Long bodegaStoreId);
    
    List<Fundraiser> findByCategory(String category);
    
    @Query("SELECT f FROM Fundraiser f WHERE f.active = true ORDER BY (f.currentAmount / f.goalAmount) DESC")
    List<Fundraiser> findTopProgressFundraisers();
    
    @Query("SELECT f FROM Fundraiser f WHERE f.active = true ORDER BY SIZE(f.donations) DESC")
    List<Fundraiser> findMostSupportedFundraisers();
}