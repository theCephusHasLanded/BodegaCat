package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.models.DiscountProgram;
import com.lkhn.ecommerce.repositories.DiscountProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BenefitsVerificationService {

    @Autowired
    private DiscountProgramRepository discountProgramRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Verifies eligibility for a specific benefits program
     *
     * @param programCode The code of the program to verify
     * @param verificationData Map containing verification data (SSN, ID number, etc.)
     * @return true if eligible, false otherwise
     */
    public boolean verifyEligibility(String programCode, Map<String, String> verificationData) {
        Optional<DiscountProgram> program = discountProgramRepository.findByProgramCode(programCode);

        if (program.isEmpty() || program.get().getVerificationEndpoint() == null) {
            // If no verification endpoint is available, simulate verification
            return simulateVerification(programCode, verificationData);
        }

        try {
            // In a real implementation, you would call the actual verification API
            // This is a simplified example
            String endpoint = program.get().getVerificationEndpoint();

            // Make API call to verification service
            // Note: In production, this would use proper security measures
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                endpoint,
                verificationData,
                Map.class
            );

            return response != null && Boolean.TRUE.equals(response.get("eligible"));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error verifying benefits eligibility: " + e.getMessage());

            // Fall back to simulation for development/testing
            return simulateVerification(programCode, verificationData);
        }
    }

    /**
     * Simulates verification for development and testing
     */
    private boolean simulateVerification(String programCode, Map<String, String> verificationData) {
        // For development purposes, always return true
        // In production, this would be replaced with actual verification logic
        return true;
    }

    /**
     * Checks if a product is eligible for purchase with a specific benefit program
     */
    public boolean isProductEligibleForProgram(Long productId, String programCode) {
        // This would typically query the database to check eligibility
        // For now, we'll return true for SNAP-eligible products
        return "SNAP".equals(programCode) || "EBT".equals(programCode);
    }

    /**
     * Gets the available balance for a benefits program
     */
    public Map<String, Object> getBenefitsBalance(String programCode, String benefitCardToken) {
        // In a real implementation, this would call the benefits program API
        // This is a simplified example that returns mock data
        Map<String, Object> balance = new HashMap<>();

        switch (programCode) {
            case "SNAP":
            case "EBT":
                balance.put("availableBalance", 250.00);
                balance.put("lastUpdated", System.currentTimeMillis());
                break;
            case "WIC":
                balance.put("availableBalance", 150.00);
                balance.put("lastUpdated", System.currentTimeMillis());
                break;
            default:
                balance.put("availableBalance", 0.00);
                balance.put("lastUpdated", System.currentTimeMillis());
        }

        return balance;
    }
}
