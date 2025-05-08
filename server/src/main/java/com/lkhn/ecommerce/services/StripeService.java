package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.models.PaymentProfile;
import com.lkhn.ecommerce.repositories.PaymentProfileRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Token;
import com.stripe.param.CustomerCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Autowired
    private PaymentProfileRepository paymentProfileRepository;

    @Transactional
    public PaymentProfile createOrUpdateCustomer(Long userId, String email, String name) throws StripeException {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
                .orElse(new PaymentProfile());

        if (profile.getStripeCustomerId() == null) {
            // Create a new Stripe customer
            CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .setDescription("Customer for user ID: " + userId)
                .build();

            Customer customer = Customer.create(params);
            profile.setStripeCustomerId(customer.getId());
            profile.setUserId(userId);
        }

        return paymentProfileRepository.save(profile);
    }

    @Transactional
    public PaymentProfile addPaymentMethod(Long userId, String paymentMethodId) throws StripeException {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        // Attach payment method to customer
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", profile.getStripeCustomerId());
        paymentMethod.attach(params);

        return profile;
    }

    @Transactional
    public PaymentProfile tokenizeEbtCard(Long userId, String cardNumber, String expiryMonth,
                                         String expiryYear, String cvc) throws StripeException {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        // In a real implementation, you would use a specialized EBT processor
        // This is a simplified example using Stripe's token API
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", cardNumber);
        cardParams.put("exp_month", expiryMonth);
        cardParams.put("exp_year", expiryYear);
        cardParams.put("cvc", cvc);

        Map<String, Object> tokenParams = new HashMap<>();
        tokenParams.put("card", cardParams);

        // Create a token (in production, use a PCI-compliant solution)
        Token token = Token.create(tokenParams);

        // Store only the token ID, never the actual card details
        profile.setEbtTokenId(token.getId());

        return paymentProfileRepository.save(profile);
    }

    @Transactional
    public PaymentProfile verifyBenefitsEligibility(Long userId, String programCode) {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        // In a real implementation, you would call the actual benefits verification API
        // This is a simplified example that just adds the program to the user's profile

        // Simulate verification process
        boolean verified = simulateVerification(programCode);

        if (verified) {
            profile.getEnrolledBenefitPrograms().add(programCode);
            if ("SNAP".equals(programCode) || "EBT".equals(programCode)) {
                profile.setEbtVerified(true);
            }
        }

        return paymentProfileRepository.save(profile);
    }

    @Transactional
    public Map<String, Object> processMixedPayment(Long userId, String orderId,
                                                 double benefitsAmount, double regularAmount,
                                                 String regularPaymentMethodId) throws StripeException {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        Map<String, Object> result = new HashMap<>();

        // 1. Process the benefits portion (simplified example)
        if (benefitsAmount > 0) {
            if (!profile.isEbtVerified() || profile.getEbtTokenId() == null) {
                throw new IllegalStateException("EBT card not verified or not on file");
            }

            // In a real implementation, you would call the EBT processor API
            boolean benefitsSuccess = processBenefitsPayment(profile.getEbtTokenId(), benefitsAmount, orderId);
            result.put("benefitsProcessed", benefitsSuccess);
            result.put("benefitsAmount", benefitsAmount);

            if (!benefitsSuccess) {
                throw new IllegalStateException("Benefits payment processing failed");
            }
        }

        // 2. Process the regular portion with Stripe
        if (regularAmount > 0) {
            Map<String, Object> paymentParams = new HashMap<>();
            paymentParams.put("amount", Math.round(regularAmount * 100)); // Convert to cents
            paymentParams.put("currency", "usd");
            paymentParams.put("customer", profile.getStripeCustomerId());
            paymentParams.put("payment_method", regularPaymentMethodId);
            paymentParams.put("confirm", true);
            paymentParams.put("description", "Order: " + orderId);

            // In a real implementation, you would create a PaymentIntent
            // This is a simplified example
            result.put("regularProcessed", true);
            result.put("regularAmount", regularAmount);
        }

        return result;
    }

    // Simulate verification with a benefits program
    private boolean simulateVerification(String programCode) {
        // In a real implementation, this would call an external API
        // For now, we'll just return true for testing
        return true;
    }

    // Simulate processing a benefits payment
    private boolean processBenefitsPayment(String tokenId, double amount, String orderId) {
        // In a real implementation, this would call an EBT processor API
        // For now, we'll just return true for testing
        return true;
    }
}
