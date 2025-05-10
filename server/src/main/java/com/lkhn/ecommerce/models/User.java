package com.lkhn.ecommerce.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @NotBlank
    private String role = "USER"; // Default role
    
    private String profileImageUrl;
    
    private int passportPoints = 0;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Membership> memberships = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Donation> donations = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BodegaVisit> visits = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "user_achievement",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    private Set<Achievement> achievements = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "user_favorite_cats",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "cat_id")
    )
    private Set<Cat> favoriteCats = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getPassportPoints() {
        return passportPoints;
    }

    public void setPassportPoints(int passportPoints) {
        this.passportPoints = passportPoints;
    }
    
    public void addPassportPoints(int points) {
        this.passportPoints += points;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }
    
    public Membership getActiveMembership() {
        return memberships.stream()
                .filter(Membership::isActive)
                .findFirst()
                .orElse(null);
    }

    public Set<Donation> getDonations() {
        return donations;
    }

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public Set<BodegaVisit> getVisits() {
        return visits;
    }

    public void setVisits(Set<BodegaVisit> visits) {
        this.visits = visits;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
        this.achievements = achievements;
    }
    
    public void addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
        this.passportPoints += achievement.getPointsValue();
    }

    public Set<Cat> getFavoriteCats() {
        return favoriteCats;
    }

    public void setFavoriteCats(Set<Cat> favoriteCats) {
        this.favoriteCats = favoriteCats;
    }
    
    public void addFavoriteCat(Cat cat) {
        this.favoriteCats.add(cat);
    }
    
    public void removeFavoriteCat(Cat cat) {
        this.favoriteCats.remove(cat);
    }
}
