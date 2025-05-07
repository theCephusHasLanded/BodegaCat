package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.BodegaStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodegaStoreRepository extends JpaRepository<BodegaStore, Long> {
    List<BodegaStore> findByNeighborhood(String neighborhood);
    
    @Query("SELECT b FROM BodegaStore b WHERE b.name LIKE %?1% OR b.description LIKE %?1% OR b.neighborhood LIKE %?1%")
    List<BodegaStore> searchBodegas(String keyword);
    
    @Query("SELECT b FROM BodegaStore b JOIN b.cats c WHERE c.id = ?1")
    BodegaStore findByBodegaWithCat(Long catId);
    
    @Query("SELECT b FROM BodegaStore b JOIN b.fundraisers f WHERE f.active = true ORDER BY SIZE(f) DESC")
    List<BodegaStore> findBodegasWithActiveFundraisers();
}