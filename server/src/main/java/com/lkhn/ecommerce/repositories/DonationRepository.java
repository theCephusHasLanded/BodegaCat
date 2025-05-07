package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUserId(Long userId);
    
    List<Donation> findByFundraiserId(Long fundraiserId);
    
    @Query("SELECT d FROM Donation d WHERE d.donationDate BETWEEN ?1 AND ?2")
    List<Donation> findDonationsInPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.fundraiser.id = ?1")
    Double getTotalDonationsForFundraiser(Long fundraiserId);
    
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.user.id = ?1")
    Double getTotalDonationsByUser(Long userId);
}