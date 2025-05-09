package com.lkhn.ecommerce.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

public class PaymentRequestDTO {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @DecimalMin(value = "0.0", message = "Benefits amount must be non-negative")
    private double benefitsAmount;

    @DecimalMin(value = "0.0", message = "Regular amount must be non-negative")
    private double regularAmount;

    @NotBlank(message = "Payment method ID is required when regular amount is greater than zero")
    private String regularPaymentMethodId;

    @Pattern(regexp = "^(EBT|SNAP|WIC)$", message = "Benefits program must be EBT, SNAP, or WIC")
    private String benefitsProgram;

    private Map<String, Object> metadata;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getBenefitsAmount() {
        return benefitsAmount;
    }

    public void setBenefitsAmount(double benefitsAmount) {
        this.benefitsAmount = benefitsAmount;
    }

    public double getRegularAmount() {
        return regularAmount;
    }

    public void setRegularAmount(double regularAmount) {
        this.regularAmount = regularAmount;
    }

    public String getRegularPaymentMethodId() {
        return regularPaymentMethodId;
    }

    public void setRegularPaymentMethodId(String regularPaymentMethodId) {
        this.regularPaymentMethodId = regularPaymentMethodId;
    }

    public String getBenefitsProgram() {
        return benefitsProgram;
    }

    public void setBenefitsProgram(String benefitsProgram) {
        this.benefitsProgram = benefitsProgram;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    // Custom validation
    public boolean isValid() {
        if (benefitsAmount <= 0 && regularAmount <= 0) {
            return false; // At least one amount must be positive
        }

        if (regularAmount > 0 && (regularPaymentMethodId == null || regularPaymentMethodId.isEmpty())) {
            return false; // Payment method required for regular amount
        }

        if (benefitsAmount > 0 && (benefitsProgram == null || benefitsProgram.isEmpty())) {
            return false; // Benefits program required for benefits amount
        }

        return true;
    }
}
