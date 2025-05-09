package com.lkhn.ecommerce.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bodega_stores")
public class BodegaStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @NotBlank
    private String address;
    
    @NotBlank
    private String neighborhood;
    
    private String description;
    
    private String imageUrl;
    
    @OneToMany(mappedBy = "bodegaStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cat> cats = new HashSet<>();
    
    @OneToMany(mappedBy = "bodegaStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BodegaVisit> visits = new HashSet<>();
    
    @OneToMany(mappedBy = "bodegaStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Fundraiser> fundraisers = new HashSet<>();

    public BodegaStore() {
    }

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

    public Set<Cat> getCats() {
        return cats;
    }

    public void setCats(Set<Cat> cats) {
        this.cats = cats;
    }

    public Set<BodegaVisit> getVisits() {
        return visits;
    }

    public void setVisits(Set<BodegaVisit> visits) {
        this.visits = visits;
    }

    public Set<Fundraiser> getFundraisers() {
        return fundraisers;
    }

    public void setFundraisers(Set<Fundraiser> fundraisers) {
        this.fundraisers = fundraisers;
    }
}