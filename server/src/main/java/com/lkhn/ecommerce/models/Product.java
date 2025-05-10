package com.lkhn.ecommerce.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Positive
    private BigDecimal price;

    private String imageUrl;

    private Integer stockQuantity;

    // Benefits-related fields
    private boolean snapEligible;

    @Enumerated(EnumType.STRING)
    private BenefitsCategory benefitsCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_discount_programs",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private Set<DiscountProgram> discountPrograms = new HashSet<>();

    // Standard getters and setters
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

    public Set<DiscountProgram> getDiscountPrograms() {
        return discountPrograms;
    }

    public void setDiscountPrograms(Set<DiscountProgram> discountPrograms) {
        this.discountPrograms = discountPrograms;
    }
}
