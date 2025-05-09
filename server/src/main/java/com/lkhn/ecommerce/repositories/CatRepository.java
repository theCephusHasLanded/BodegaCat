package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByBodegaStoreId(Long bodegaStoreId);
    Optional<Cat> findByName(String name);
}