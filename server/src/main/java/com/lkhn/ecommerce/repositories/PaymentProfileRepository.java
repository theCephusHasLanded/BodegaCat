package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.PaymentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentProfileRepository extends JpaRepository<PaymentProfile, Long> {
    Optional<PaymentProfile> findByUserId(Long userId);
    Optional<PaymentProfile> findByStripeCustomerId(String stripeCustomerId);
}