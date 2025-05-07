package com.lkhn.ecommerce.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bodega_visits")
public class BodegaVisit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "bodega_store_id")
    private BodegaStore bodegaStore;
    
    @NotNull
    private LocalDateTime visitDate;
    
    private String comment;
    
    private Integer rating; // 1-5 stars
    
    private String photoUrl;
    
    private boolean verified = false;
    
    public BodegaVisit() {
        this.visitDate = LocalDateTime.now();
    }
    
    public BodegaVisit(User user, BodegaStore bodegaStore) {
        this.user = user;
        this.bodegaStore = bodegaStore;
        this.visitDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BodegaStore getBodegaStore() {
        return bodegaStore;
    }

    public void setBodegaStore(BodegaStore bodegaStore) {
        this.bodegaStore = bodegaStore;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}