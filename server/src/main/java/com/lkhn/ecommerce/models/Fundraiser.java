package com.lkhn.ecommerce.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fundraisers")
public class Fundraiser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String title;
    
    @NotBlank
    @Column(length = 1000)
    private String description;
    
    @NotNull
    private Double goalAmount;
    
    private Double currentAmount = 0.0;
    
    @NotNull
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private String imageUrl;
    
    @ManyToOne
    @JoinColumn(name = "bodega_store_id")
    private BodegaStore bodegaStore;
    
    @OneToMany(mappedBy = "fundraiser", cascade = CascadeType.ALL)
    private Set<Donation> donations = new HashSet<>();
    
    private boolean active = true;
    
    private String category; // e.g., "RENOVATION", "EQUIPMENT", "CAT_CARE", "COMMUNITY_EVENT"
    
    public Fundraiser() {
    }
    
    public Fundraiser(String title, String description, Double goalAmount, LocalDateTime startDate, BodegaStore bodegaStore) {
        this.title = title;
        this.description = description;
        this.goalAmount = goalAmount;
        this.startDate = startDate;
        this.bodegaStore = bodegaStore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BodegaStore getBodegaStore() {
        return bodegaStore;
    }

    public void setBodegaStore(BodegaStore bodegaStore) {
        this.bodegaStore = bodegaStore;
    }

    public Set<Donation> getDonations() {
        return donations;
    }

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public void addDonation(Donation donation) {
        this.donations.add(donation);
        this.currentAmount += donation.getAmount();
        donation.setFundraiser(this);
    }
    
    public double getProgressPercentage() {
        if (goalAmount <= 0) {
            return 0;
        }
        return (currentAmount / goalAmount) * 100;
    }
    
    public boolean isGoalReached() {
        return currentAmount >= goalAmount;
    }
}