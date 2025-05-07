package com.lkhn.ecommerce.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "memberships")
public class Membership {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String tier; // "CASUAL", "REGULAR", "CHAMPION"
    
    @NotNull
    private Double monthlyFee;
    
    @NotNull
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private boolean active = true;
    
    public Membership() {
    }
    
    public Membership(String tier, Double monthlyFee, LocalDateTime startDate, User user) {
        this.tier = tier;
        this.monthlyFee = monthlyFee;
        this.startDate = startDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee = monthlyFee;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}