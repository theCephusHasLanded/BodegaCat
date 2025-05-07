package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByBodegaStoreId(Long bodegaStoreId);
    
    List<Cat> findByFeatured(boolean featured);
    
    @Query("SELECT c FROM Cat c ORDER BY c.likesCount DESC")
    List<Cat> findMostPopularCats();
    
    @Query("SELECT c FROM Cat c JOIN c.bodegaStore b WHERE b.neighborhood = ?1")
    List<Cat> findCatsByNeighborhood(String neighborhood);
}