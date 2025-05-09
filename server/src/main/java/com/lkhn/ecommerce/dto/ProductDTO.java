package com.lkhn.ecommerce.dto;

import com.lkhn.ecommerce.models.BenefitsCategory;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Integer stockQuantity;
    private boolean snapEligible;
    private BenefitsCategory benefitsCategory;
    private Set<String> discountPrograms = new HashSet<>();

    // Constructors
    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, BigDecimal price, String imageUrl,
                     Integer stockQuantity, boolean snapEligible, BenefitsCategory benefitsCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.snapEligible = snapEligible;
        this.benefitsCategory = benefitsCategory;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isSnapEligible() {
        return snapEligible;
    }

    public void setSnapEligible(boolean snapEligible) {
        this.snapEligible = snapEligible;
    }

    public BenefitsCategory getBenefitsCategory() {
        return benefitsCategory;
    }

    public void setBenefitsCategory(BenefitsCategory benefitsCategory) {
        this.benefitsCategory = benefitsCategory;
    }

    public Set<String> getDiscountPrograms() {
        return discountPrograms;
    }

    public void setDiscountPrograms(Set<String> discountPrograms) {
        this.discountPrograms = discountPrograms;
    }
}
