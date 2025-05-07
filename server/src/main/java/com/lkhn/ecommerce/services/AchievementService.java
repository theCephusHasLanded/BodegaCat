package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.models.Achievement;
import com.lkhn.ecommerce.models.User;
import com.lkhn.ecommerce.repositories.AchievementRepository;
import com.lkhn.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
    
    public Optional<Achievement> getAchievementById(Long id) {
        return achievementRepository.findById(id);
    }
    
    public List<Achievement> getAchievementsByCategory(String category) {
        return achievementRepository.findByCategory(category);
    }
    
    public Set<Achievement> getAchievementsByUser(Long userId) {
        return achievementRepository.findAchievementsByUser(userId);
    }
    
    public Set<Achievement> getAchievementsNotEarnedByUser(Long userId) {
        return achievementRepository.findAchievementsNotEarnedByUser(userId);
    }
    
    public List<Achievement> getMostEarnedAchievements() {
        return achievementRepository.findMostEarnedAchievements();
    }
    
    @Transactional
    public Achievement createAchievement(String name, String description, String category, int pointsValue) {
        Achievement achievement = new Achievement();
        achievement.setName(name);
        achievement.setDescription(description);
        achievement.setCategory(category);
        achievement.setPointsValue(pointsValue);
        
        return achievementRepository.save(achievement);
    }
    
    @Transactional
    public void grantAchievement(Long userId, Long achievementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));
        
        if (!user.getAchievements().contains(achievement)) {
            user.addAchievement(achievement);
            userRepository.save(user);
        }
    }
    
    @Transactional
    public void grantFirstDonationAchievement(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Achievement> achievements = achievementRepository.findByCategory("DONATION");
        
        Optional<Achievement> firstDonation = achievements.stream()
                .filter(a -> "First Donation".equals(a.getName()))
                .findFirst();
        
        if (firstDonation.isPresent() && !user.getAchievements().contains(firstDonation.get())) {
            user.addAchievement(firstDonation.get());
            userRepository.save(user);
        }
    }
    
    @Transactional
    public void grantDonationMilestoneAchievement(Long userId, String level) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Achievement> achievements = achievementRepository.findByCategory("DONATION");
        
        Optional<Achievement> milestone = achievements.stream()
                .filter(a -> a.getName().contains(level))
                .findFirst();
        
        if (milestone.isPresent() && !user.getAchievements().contains(milestone.get())) {
            user.addAchievement(milestone.get());
            userRepository.save(user);
        }
    }
    
    @Transactional
    public void grantVisitAchievement(Long userId, int visitCount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Achievement> achievements = achievementRepository.findByCategory("VISIT");
        
        if (visitCount == 1) {
            // First visit achievement
            Optional<Achievement> firstVisit = achievements.stream()
                    .filter(a -> "First Visit".equals(a.getName()))
                    .findFirst();
            
            if (firstVisit.isPresent() && !user.getAchievements().contains(firstVisit.get())) {
                user.addAchievement(firstVisit.get());
            }
        } else if (visitCount == 5) {
            // 5 visits achievement
            Optional<Achievement> fiveVisits = achievements.stream()
                    .filter(a -> "5 Bodega Visits".equals(a.getName()))
                    .findFirst();
            
            if (fiveVisits.isPresent() && !user.getAchievements().contains(fiveVisits.get())) {
                user.addAchievement(fiveVisits.get());
            }
        } else if (visitCount == 10) {
            // 10 visits achievement
            Optional<Achievement> tenVisits = achievements.stream()
                    .filter(a -> "10 Bodega Visits".equals(a.getName()))
                    .findFirst();
            
            if (tenVisits.isPresent() && !user.getAchievements().contains(tenVisits.get())) {
                user.addAchievement(tenVisits.get());
            }
        } else if (visitCount == 25) {
            // 25 visits achievement - Bodega Explorer
            Optional<Achievement> explorer = achievements.stream()
                    .filter(a -> "Bodega Explorer".equals(a.getName()))
                    .findFirst();
            
            if (explorer.isPresent() && !user.getAchievements().contains(explorer.get())) {
                user.addAchievement(explorer.get());
            }
        }
        
        userRepository.save(user);
    }
    
    @Transactional
    public void initializeDefaultAchievements() {
        // Donation Achievements
        if (achievementRepository.findByCategory("DONATION").isEmpty()) {
            createAchievement("First Donation", "Make your first donation to a bodega cat fundraiser", "DONATION", 10);
            createAchievement("Cat Supporter", "Donate a total of $100 to bodega cat fundraisers", "DONATION", 25);
            createAchievement("Cat Patron", "Donate a total of $500 to bodega cat fundraisers", "DONATION", 50);
            createAchievement("Cat Benefactor", "Donate a total of $1,000 to bodega cat fundraisers", "DONATION", 100);
        }
        
        // Visit Achievements
        if (achievementRepository.findByCategory("VISIT").isEmpty()) {
            createAchievement("First Visit", "Record your first visit to a bodega", "VISIT", 10);
            createAchievement("5 Bodega Visits", "Visit 5 different bodegas", "VISIT", 25);
            createAchievement("10 Bodega Visits", "Visit 10 different bodegas", "VISIT", 50);
            createAchievement("Bodega Explorer", "Visit 25 different bodegas", "VISIT", 100);
            createAchievement("Neighborhood Enthusiast", "Visit 5 bodegas in the same neighborhood", "VISIT", 30);
        }
        
        // Social Achievements
        if (achievementRepository.findByCategory("SOCIAL").isEmpty()) {
            createAchievement("Cat Lover", "Add 5 cats to your favorites", "SOCIAL", 15);
            createAchievement("Cat Photographer", "Upload 3 cat photos", "SOCIAL", 20);
            createAchievement("Community Member", "Attend your first bodega cat event", "SOCIAL", 25);
            createAchievement("Social Butterfly", "Share 5 bodega cat stories on social media", "SOCIAL", 15);
        }
        
        // Membership Achievements
        if (achievementRepository.findByCategory("MEMBERSHIP").isEmpty()) {
            createAchievement("Regular Member", "Become a Regular member", "MEMBERSHIP", 20);
            createAchievement("Champion Member", "Become a Champion member", "MEMBERSHIP", 50);
            createAchievement("Loyal Supporter", "Maintain membership for 6 months", "MEMBERSHIP", 75);
        }
    }
}