package com.lkhn.ecommerce.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class DonationRequest {
    
    @NotNull
    @Positive
    private Double amount;
    
    private String message;
    
    private boolean anonymous = false;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}