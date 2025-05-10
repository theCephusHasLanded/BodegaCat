package com.lkhn.ecommerce.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BodegaStoreRequest {
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @NotBlank
    private String address;
    
    @NotBlank
    private String neighborhood;
    
    private String description;
    
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}