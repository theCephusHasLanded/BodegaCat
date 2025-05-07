package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByUserId(Long userId);
    
    Optional<Membership> findByUserIdAndActive(Long userId, boolean active);
    
    List<Membership> findByTier(String tier);
    
    @Query("SELECT COUNT(m) FROM Membership m WHERE m.active = true AND m.tier = ?1")
    long countActiveMembershipsByTier(String tier);
    
    @Query("SELECT m FROM Membership m WHERE m.active = true")
    List<Membership> findAllActiveMemberships();
}