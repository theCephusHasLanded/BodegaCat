package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Fundraiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundraiserRepository extends JpaRepository<Fundraiser, Long> {
    List<Fundraiser> findByCreatorId(Long creatorId);
    Optional<Fundraiser> findByTitle(String title);
}