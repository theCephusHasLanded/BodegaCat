package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.BodegaStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodegaStoreRepository extends JpaRepository<BodegaStore, Long> {
    List<BodegaStore> findByNeighborhood(String neighborhood);
    Optional<BodegaStore> findByName(String name);
}