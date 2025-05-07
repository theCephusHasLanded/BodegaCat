package com.lkhn.ecommerce.dto;

import com.lkhn.ecommerce.models.BenefitsCategory;

import java.math.BigDecimal;
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
    private Set<String> discountPrograms;

    // Getters and setters
    // Constructor
}
