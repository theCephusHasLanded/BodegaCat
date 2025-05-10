package com.lkhn.ecommerce.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class FundraiserRequest {
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String description;
    
    @NotNull
    @Positive
    private Double goalAmount;
    
    private Long bodegaStoreId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(Double goalAmount) {
        this.goalAmount = goalAmount;
    }

    public Long getBodegaStoreId() {
        return bodegaStoreId;
    }

    public void setBodegaStoreId(Long bodegaStoreId) {
        this.bodegaStoreId = bodegaStoreId;
    }
}