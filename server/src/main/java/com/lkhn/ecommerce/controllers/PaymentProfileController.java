package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.models.PaymentProfile;
import com.lkhn.ecommerce.payload.PaymentRequestDTO;
import com.lkhn.ecommerce.services.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentProfileController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createOrUpdateProfile(
            Authentication authentication,
            @RequestBody Map<String, String> profileData) {

        try {
            // In a real app, get userId from authentication
            Long userId = 1L; // Placeholder
            String email = profileData.get("email");
            String name = profileData.get("name");

            PaymentProfile profile = stripeService.createOrUpdateCustomer(userId, email, name);
            return ResponseEntity.ok(Map.of(
                "message", "Payment profile created/updated successfully",
                "profileId", profile.getId()
            ));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to create payment profile",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/methods")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addPaymentMethod(
            Authentication authentication,
            @RequestBody Map<String, String> paymentData) {

        try {
            // In a real app, get userId from authentication
            Long userId = 1L; // Placeholder
            String paymentMethodId = paymentData.get("paymentMethodId");

            PaymentProfile profile = stripeService.addPaymentMethod(userId, paymentMethodId);
            return ResponseEntity.ok(Map.of(
                "message", "Payment method added successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to add payment method",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/benefits/ebt")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addEbtCard(
            Authentication authentication,
            @RequestBody Map<String, String> cardData) {

        try {
            // In a real app, get userId from authentication
            Long userId = 1L; // Placeholder

            // In a production app, this should be handled with a secure iframe
            // to avoid handling sensitive card data directly
            PaymentProfile profile = stripeService.tokenizeEbtCard(
                userId,
                cardData.get("cardNumber"),
                cardData.get("expiryMonth"),
                cardData.get("expiryYear"),
                cardData.get("cvc")
            );

            return ResponseEntity.ok(Map.of(
                "message", "EBT card added successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to add EBT card",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/benefits/verify/{programCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyBenefitsEligibility(
            Authentication authentication,
            @PathVariable String programCode) {

        try {
            // In a real app, get userId from authentication
            Long userId = 1L; // Placeholder

            PaymentProfile profile = stripeService.verifyBenefitsEligibility(userId, programCode);
            return ResponseEntity.ok(Map.of(
                "message", "Benefits eligibility verified successfully",
                "programCode", programCode,
                "verified", true
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to verify benefits eligibility",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/checkout/mixed")
@PreAuthorize("isAuthenticated()")
public ResponseEntity<?> processMixedPayment(
        Authentication authentication,
        @Valid @RequestBody PaymentRequestDTO paymentRequest,
        BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
        List<String> errors = bindingResult.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Validation failed",
            "details", errors
        ));
    }

    if (!paymentRequest.isValid()) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Invalid payment request",
            "message", "Payment request validation failed"
        ));
    }

    try {
        // In a real app, get userId from authentication
        Long userId = 1L; // Placeholder

        Map<String, Object> result = stripeService.processMixedPayment(
            userId,
            paymentRequest.getOrderId(),
            paymentRequest.getBenefitsAmount(),
            paymentRequest.getRegularAmount(),
            paymentRequest.getRegularPaymentMethodId()
        );

        return ResponseEntity.ok(result);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Payment processing failed",
            "message", e.getMessage()
        ));
    }
}
}
