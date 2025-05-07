package com.lkhn.ecommerce.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "cats")
public class Cat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    private String name;
    
    private String breed;
    
    private String color;
    
    private String imageUrl;
    
    @Size(max = 500)
    private String bio;
    
    private LocalDate dateOfBirth;
    
    private LocalDate dateJoined;
    
    @ManyToOne
    @JoinColumn(name = "bodega_store_id")
    private BodegaStore bodegaStore;
    
    private boolean featured = false;
    
    private int likesCount = 0;
    
    public Cat() {
    }
    
    public Cat(String name, BodegaStore bodegaStore) {
        this.name = name;
        this.bodegaStore = bodegaStore;
        this.dateJoined = LocalDate.now();
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public BodegaStore getBodegaStore() {
        return bodegaStore;
    }

    public void setBodegaStore(BodegaStore bodegaStore) {
        this.bodegaStore = bodegaStore;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
    
    public void incrementLikes() {
        this.likesCount++;
    }
}