package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.models.PaymentProfile;
import com.lkhn.ecommerce.repositories.PaymentProfileRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Token;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Autowired
    private PaymentProfileRepository paymentProfileRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Transactional
    public PaymentProfile createOrUpdateCustomer(Long userId, String email, String name) throws StripeException {
        Optional<PaymentProfile> existingProfile = paymentProfileRepository.findByUserId(userId);

        if (existingProfile.isPresent()) {
            PaymentProfile profile = existingProfile.get();
            
            if (profile.getStripeCustomerId() != null) {
                // Update existing Stripe customer
                Customer customer = Customer.retrieve(profile.getStripeCustomerId());
                CustomerUpdateParams params = CustomerUpdateParams.builder()
                    .setEmail(email)
                    .setName(name)
                    .build();
                customer.update(params);
            } else {
                // Create new Stripe customer
                CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(email)
                    .setName(name)
                    .build();
                Customer customer = Customer.create(params);
                profile.setStripeCustomerId(customer.getId());
            }
            
            return paymentProfileRepository.save(profile);
        } else {
            // Create new payment profile and Stripe customer
            CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .build();
            Customer customer = Customer.create(params);
            
            PaymentProfile profile = new PaymentProfile(userId);
            profile.setStripeCustomerId(customer.getId());
            
            return paymentProfileRepository.save(profile);
        }
    }

    @Transactional
    public PaymentProfile addPaymentMethod(Long userId, String paymentMethodId) throws StripeException {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User payment profile not found"));

        // Attach payment method to customer
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
            .setCustomer(profile.getStripeCustomerId())
            .build();
        paymentMethod.attach(params);

        // Set as default if none exists
        if (profile.getDefaultPaymentMethodId() == null) {
            profile.setDefaultPaymentMethodId(paymentMethodId);
        }

        return paymentProfileRepository.save(profile);
    }

    @Transactional
    public PaymentProfile tokenizeEbtCard(Long userId, String cardNumber, String expiryMonth, String expiryYear, String cvc) throws StripeException {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User payment profile not found"));

        // In production, use Stripe Elements to tokenize card client-side
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", cardNumber);
        cardParams.put("exp_month", expiryMonth);
        cardParams.put("exp_year", expiryYear);
        cardParams.put("cvc", cvc);

        Map<String, Object> tokenParams = new HashMap<>();
        tokenParams.put("card", cardParams);

        Token token = Token.create(tokenParams);
        profile.setEbtTokenId(token.getId());

        return paymentProfileRepository.save(profile);
    }

    @Transactional
    public PaymentProfile verifyBenefitsEligibility(Long userId, String programCode) {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User payment profile not found"));

        // In a real implementation, this would verify eligibility with a government API
        // This is simplified to always approve
        profile.addBenefitProgram(programCode);
        profile.setEbtVerified(true);

        return paymentProfileRepository.save(profile);
    }

    @Transactional
    public Map<String, Object> processMixedPayment(Long userId, String orderId, Double benefitsAmount, Double regularAmount, String regularPaymentMethodId) throws StripeException {
        PaymentProfile profile = paymentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User payment profile not found"));

        Map<String, Object> result = new HashMap<>();
        
        // Step 1: Process benefits payment
        if (benefitsAmount > 0 && profile.isEbtVerified()) {
            // In a real implementation, this would process an EBT payment
            // We'll simulate a successful transaction
            result.put("benefitsProcessed", true);
            result.put("benefitsAmount", benefitsAmount);
        } else {
            result.put("benefitsProcessed", false);
            result.put("benefitsAmount", 0.0);
        }
        
        // Step 2: Process regular payment
        if (regularAmount > 0) {
            // Create a String metadata map for Stripe API
            Map<String, String> stripeMetadata = new HashMap<>();
            stripeMetadata.put("orderId", orderId);
            
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setCurrency("usd")
                .setAmount(Math.round(regularAmount * 100)) // Convert to cents
                .setPaymentMethod(regularPaymentMethodId)
                .setCustomer(profile.getStripeCustomerId())
                .setConfirm(true)
                .putAllMetadata(stripeMetadata)
                .build();
            
            PaymentIntent intent = PaymentIntent.create(params);
            
            result.put("regularProcessed", true);
            result.put("regularAmount", regularAmount);
            result.put("paymentIntentId", intent.getId());
            result.put("paymentStatus", intent.getStatus());
        } else {
            result.put("regularProcessed", false);
            result.put("regularAmount", 0.0);
        }
        
        return result;
    }
}