package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.models.PaymentProfile;
import com.lkhn.ecommerce.repositories.PaymentProfileRepository;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StripeServiceTest {

    @Mock
    private PaymentProfileRepository paymentProfileRepository;

    @InjectMocks
    private StripeService stripeService;

    private PaymentProfile testProfile;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a test payment profile
        testProfile = new PaymentProfile();
        testProfile.setId(1L);
        testProfile.setUserId(userId);
        testProfile.setStripeCustomerId("cus_test123");
        testProfile.setEbtVerified(true);
        testProfile.setEbtTokenId("tok_test123");

        // Mock repository responses
        when(paymentProfileRepository.findByUserId(userId)).thenReturn(Optional.of(testProfile));
        when(paymentProfileRepository.save(any(PaymentProfile.class))).thenReturn(testProfile);
    }

    @Test
    void testVerifyBenefitsEligibility() {
        // Test verifying benefits eligibility
        PaymentProfile result = stripeService.verifyBenefitsEligibility(userId, "SNAP");

        // Verify the result
        assertTrue(result.getEnrolledBenefitPrograms().contains("SNAP"));
        assertTrue(result.isEbtVerified());

        // Verify repository was called
        verify(paymentProfileRepository).save(any(PaymentProfile.class));
    }

    @Test
    void testProcessMixedPayment() throws StripeException {
        // Test processing a mixed payment
        Map<String, Object> result = stripeService.processMixedPayment(
            userId, "order123", 25.0, 15.0, "pm_test123");

        // Verify the result
        assertEquals(true, result.get("benefitsProcessed"));
        assertEquals(25.0, result.get("benefitsAmount"));
        assertEquals(true, result.get("regularProcessed"));
        assertEquals(15.0, result.get("regularAmount"));
    }

    @Test
    void testCreateOrUpdateCustomer_existingCustomer() throws StripeException {
        // Test updating an existing customer
        PaymentProfile result = stripeService.createOrUpdateCustomer(
            userId, "test@example.com", "Test User");

        // Verify the result
        assertEquals(testProfile.getStripeCustomerId(), result.getStripeCustomerId());

        // Verify repository was called but no new customer was created
        verify(paymentProfileRepository).findByUserId(userId);
        verify(paymentProfileRepository).save(any(PaymentProfile.class));
    }

    // Note: Testing actual Stripe API calls would require integration tests
    // with a test API key. These tests mock the behavior.
}