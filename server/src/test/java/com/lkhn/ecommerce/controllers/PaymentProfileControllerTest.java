package com.lkhn.ecommerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lkhn.ecommerce.dto.PaymentRequestDTO;
import com.lkhn.ecommerce.model.PaymentProfile;
import com.lkhn.ecommerce.service.StripeService;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StripeService stripeService;

    private PaymentProfile testProfile;
    private Map<String, Object> paymentResult;

    @BeforeEach
    void setUp() throws StripeException {
        // Set up test data
        testProfile = new PaymentProfile();
        testProfile.setId(1L);
        testProfile.setUserId(1L);
        testProfile.setStripeCustomerId("cus_test123");

        // Set up mock payment result
        paymentResult = new HashMap<>();
        paymentResult.put("benefitsProcessed", true);
        paymentResult.put("benefitsAmount", 25.0);
        paymentResult.put("regularProcessed", true);
        paymentResult.put("regularAmount", 15.0);

        // Configure mocks
        when(stripeService.createOrUpdateCustomer(anyLong(), anyString(), anyString()))
            .thenReturn(testProfile);

        when(stripeService.addPaymentMethod(anyLong(), anyString()))
            .thenReturn(testProfile);

        when(stripeService.tokenizeEbtCard(anyLong(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(testProfile);

        when(stripeService.verifyBenefitsEligibility(anyLong(), anyString()))
            .thenReturn(testProfile);

        when(stripeService.processMixedPayment(anyLong(), anyString(), anyDouble(), anyDouble(), anyString()))
            .thenReturn(paymentResult);
    }

    @Test
    @WithMockUser
    void testCreateOrUpdateProfile() throws Exception {
        Map<String, String> profileData = new HashMap<>();
        profileData.put("email", "test@example.com");
        profileData.put("name", "Test User");

        mockMvc.perform(post("/api/payments/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment profile created/updated successfully"))
                .andExpect(jsonPath("$.profileId").value(1));
    }

    @Test
    @WithMockUser
    void testAddPaymentMethod() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("paymentMethodId", "pm_test123");

        mockMvc.perform(post("/api/payments/methods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment method added successfully"));
    }

    @Test
    @WithMockUser
    void testProcessMixedPayment() throws Exception {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setOrderId("order123");
        paymentRequest.setBenefitsAmount(25.0);
        paymentRequest.setRegularAmount(15.0);
        paymentRequest.setRegularPaymentMethodId("pm_test123");
        paymentRequest.setBenefitsProgram("SNAP");

        mockMvc.perform(post("/api/payments/checkout/mixed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.benefitsProcessed").value(true))
                .andExpect(jsonPath("$.benefitsAmount").value(25.0))
                .andExpect(jsonPath("$.regularProcessed").value(true))
                .andExpect(jsonPath("$.regularAmount").value(15.0));
    }

    @Test
    @WithMockUser
    void testProcessMixedPayment_ValidationFailure() throws Exception {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        // Missing required fields

        mockMvc.perform(post("/api/payments/checkout/mixed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    @WithMockUser
    void testVerifyBenefitsEligibility() throws Exception {
        Map<String, String> verificationData = new HashMap<>();
        verificationData.put("benefitsProgram", "SNAP");

        mockMvc.perform(post("/api/payments/benefits/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verificationData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Benefits eligibility verified successfully"));
    }

    @Test
    @WithMockUser
    void testTokenizeEbtCard() throws Exception {
        Map<String, String> ebtData = new HashMap<>();
        ebtData.put("cardNumber", "1234567890123456");
        ebtData.put("expiryMonth", "12");
        ebtData.put("expiryYear", "2025");
        ebtData.put("pin", "1234");

        mockMvc.perform(post("/api/payments/ebt/tokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ebtData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("EBT card tokenized successfully"));
    }

    @Test
    void testUnauthenticatedAccess() throws Exception {
        // Test without authentication
        mockMvc.perform(post("/api/payments/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }
}
