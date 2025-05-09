package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByFundraiserId(Long fundraiserId);
    List<Donation> findByUserId(Long userId);
}