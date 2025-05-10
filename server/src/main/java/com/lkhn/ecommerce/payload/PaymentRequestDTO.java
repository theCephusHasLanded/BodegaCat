package com.lkhn.ecommerce.payload;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PaymentRequestDTO {
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotNull(message = "Benefits amount must be specified")
    @Min(value = 0, message = "Benefits amount must be 0 or positive")
    private Double benefitsAmount;
    
    @NotNull(message = "Regular amount must be specified")
    @Min(value = 0, message = "Regular amount must be 0 or positive")
    private Double regularAmount;
    
    @NotBlank(message = "Regular payment method ID is required")
    private String regularPaymentMethodId;
    
    // Default constructor
    public PaymentRequestDTO() {
    }
    
    // Constructor with all fields
    public PaymentRequestDTO(String orderId, Double benefitsAmount, Double regularAmount, String regularPaymentMethodId) {
        this.orderId = orderId;
        this.benefitsAmount = benefitsAmount;
        this.regularAmount = regularAmount;
        this.regularPaymentMethodId = regularPaymentMethodId;
    }
    
    // Getters and setters
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public Double getBenefitsAmount() {
        return benefitsAmount;
    }
    
    public void setBenefitsAmount(Double benefitsAmount) {
        this.benefitsAmount = benefitsAmount;
    }
    
    public Double getRegularAmount() {
        return regularAmount;
    }
    
    public void setRegularAmount(Double regularAmount) {
        this.regularAmount = regularAmount;
    }
    
    public String getRegularPaymentMethodId() {
        return regularPaymentMethodId;
    }
    
    public void setRegularPaymentMethodId(String regularPaymentMethodId) {
        this.regularPaymentMethodId = regularPaymentMethodId;
    }
    
    // Validation method
    public boolean isValid() {
        // At least one payment amount must be greater than 0
        return (benefitsAmount > 0 || regularAmount > 0);
    }
}