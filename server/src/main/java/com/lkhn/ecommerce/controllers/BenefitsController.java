package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.services.BenefitsVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/benefits")
public class BenefitsController {

    @Autowired
    private BenefitsVerificationService benefitsVerificationService;

    @GetMapping("/eligibility/{programCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> checkEligibility(
            @PathVariable String programCode,
            @RequestBody Map<String, String> verificationData) {

        boolean eligible = benefitsVerificationService.verifyEligibility(
            programCode, verificationData);

        return ResponseEntity.ok(Map.of(
            "programCode", programCode,
            "eligible", eligible
        ));
    }

    @GetMapping("/balance/{programCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> checkBalance(
            Authentication authentication,
            @PathVariable String programCode,
            @RequestParam String benefitCardToken) {

        Map<String, Object> balance = benefitsVerificationService.getBenefitsBalance(
            programCode, benefitCardToken);

        return ResponseEntity.ok(balance);
    }

    @GetMapping("/product-eligibility/{productId}/{programCode}")
    public ResponseEntity<?> checkProductEligibility(
            @PathVariable Long productId,
            @PathVariable String programCode) {

        boolean eligible = benefitsVerificationService.isProductEligibleForProgram(
            productId, programCode);

        return ResponseEntity.ok(Map.of(
            "productId", productId,
            "programCode", programCode,
            "eligible", eligible
        ));
    }
}
