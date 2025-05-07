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
    @Size(max = 255)
    private String address;
    
    @Size(max = 500)
    private String description;
    
    private String neighborhood;
    
    private String imageUrl;
    
    private String ownerName;
    
    private Integer yearEstablished;
    
    @OneToMany(mappedBy = "bodegaStore", cascade = CascadeType.ALL)
    private Set<Cat> cats = new HashSet<>();
    
    @OneToMany(mappedBy = "bodegaStore", cascade = CascadeType.ALL)
    private Set<Fundraiser> fundraisers = new HashSet<>();
    
    public BodegaStore() {
    }
    
    public BodegaStore(String name, String address, String neighborhood) {
        this.name = name;
        this.address = address;
        this.neighborhood = neighborhood;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getYearEstablished() {
        return yearEstablished;
    }

    public void setYearEstablished(Integer yearEstablished) {
        this.yearEstablished = yearEstablished;
    }

    public Set<Cat> getCats() {
        return cats;
    }

    public void setCats(Set<Cat> cats) {
        this.cats = cats;
    }

    public Set<Fundraiser> getFundraisers() {
        return fundraisers;
    }

    public void setFundraisers(Set<Fundraiser> fundraisers) {
        this.fundraisers = fundraisers;
    }
    
    public void addCat(Cat cat) {
        this.cats.add(cat);
        cat.setBodegaStore(this);
    }
    
    public void removeCat(Cat cat) {
        this.cats.remove(cat);
        cat.setBodegaStore(null);
    }
    
    public void addFundraiser(Fundraiser fundraiser) {
        this.fundraisers.add(fundraiser);
        fundraiser.setBodegaStore(this);
    }
    
    public void removeFundraiser(Fundraiser fundraiser) {
        this.fundraisers.remove(fundraiser);
        fundraiser.setBodegaStore(null);
    }
}