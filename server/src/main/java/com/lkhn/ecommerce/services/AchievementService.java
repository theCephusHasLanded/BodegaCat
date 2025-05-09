package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.exception.ResourceNotFoundException;
import com.lkhn.ecommerce.models.Achievement;
import com.lkhn.ecommerce.models.User;
import com.lkhn.ecommerce.repositories.AchievementRepository;
import com.lkhn.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private Map<String, Achievement> achievementCache = new HashMap<>();
    
    @PostConstruct
    public void initDefaultAchievements() {
        // Only initialize if repository is empty
        if (achievementRepository.count() == 0) {
            // Donation achievements
            createAchievement("FIRST_DONATION", "First Donation", "Make your first donation to any fundraiser", "DONATION", 10);
            createAchievement("SUPPORTER", "Supporter", "Donate $5 or more to a fundraiser", "DONATION", 20);
            createAchievement("BENEFACTOR", "Benefactor", "Donate $25 or more to a fundraiser", "DONATION", 50);
            createAchievement("CHAMPION", "Champion", "Donate $100 or more to a fundraiser", "DONATION", 100);
            createAchievement("REGULAR_SUPPORTER", "Regular Supporter", "Donate a total of $50 across all fundraisers", "DONATION", 30);
            createAchievement("COMMUNITY_PILLAR", "Community Pillar", "Donate a total of $250 across all fundraisers", "DONATION", 150);
            
            // Visit achievements
            createAchievement("FIRST_VISIT", "First Visit", "Record your first bodega visit", "VISIT", 10);
            createAchievement("REGULAR_VISITOR", "Regular Visitor", "Visit 5 bodegas", "VISIT", 20);
            createAchievement("BODEGA_EXPLORER", "Bodega Explorer", "Visit 20 bodegas", "VISIT", 50);
            createAchievement("BODEGA_ENTHUSIAST", "Bodega Enthusiast", "Visit 50 bodegas", "VISIT", 100);
            createAchievement("NEIGHBORHOOD_EXPLORER", "Neighborhood Explorer", "Visit bodegas in 3 different neighborhoods", "VISIT", 30);
            createAchievement("CITY_EXPLORER", "City Explorer", "Visit bodegas in 10 different neighborhoods", "VISIT", 150);
            
            // Social achievements
            createAchievement("SOCIAL_BUTTERFLY", "Social Butterfly", "Leave reviews on 10 different bodegas", "SOCIAL", 30);
            createAchievement("PHOTOGRAPHER", "Cat Photographer", "Share photos of 5 different bodega cats", "SOCIAL", 40);
            
            // Membership achievements
            createAchievement("REGULAR_MEMBER", "Regular Member", "Join as a Regular member", "MEMBERSHIP", 20);
            createAchievement("CHAMPION_MEMBER", "Champion Member", "Join as a Champion member", "MEMBERSHIP", 50);
            createAchievement("LOYAL_SUPPORTER", "Loyal Supporter", "Maintain membership for 6 months", "MEMBERSHIP", 100);
        }
        
        // Cache achievements for faster lookup
        cacheAchievements();
    }
    
    private void cacheAchievements() {
        achievementCache.clear();
        achievementRepository.findAll().forEach(a -> achievementCache.put(a.getName(), a));
    }
    
    private Achievement createAchievement(String name, String title, String description, String category, int pointsValue) {
        Achievement achievement = new Achievement();
        achievement.setName(name);
        achievement.setTitle(title);
        achievement.setDescription(description);
        achievement.setCategory(category);
        achievement.setPointsValue(pointsValue);
        return achievementRepository.save(achievement);
    }
    
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
    
    public List<String> getAllAchievementCategories() {
        return achievementRepository.findAll().stream()
                .map(Achievement::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<Achievement> getUserAchievements(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return new ArrayList<>(user.getAchievements());
    }
    
    @Transactional
    public boolean checkAndGrantAchievement(Long userId, String achievementName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Achievement achievement = achievementCache.getOrDefault(achievementName, 
                achievementRepository.findByName(achievementName)
                    .orElseThrow(() -> new ResourceNotFoundException("Achievement not found: " + achievementName)));
        
        // Check if user already has this achievement
        if (user.getAchievements().contains(achievement)) {
            return false;
        }
        
        // Grant achievement
        user.getAchievements().add(achievement);
        user.setPassportPoints(user.getPassportPoints() + achievement.getPointsValue());
        userRepository.save(user);
        return true;
    }
    
    public Map<String, Object> getUserAchievementProgress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Set<Achievement> userAchievements = user.getAchievements();
        Map<String, Object> progress = new HashMap<>();
        
        // For now, just provide simple progress stats
        // In a real app, this would calculate actual progress toward each achievement
        progress.put("totalAchievements", userAchievements.size());
        progress.put("totalPoints", user.getPassportPoints());
        progress.put("achievementsByCategory", groupAchievementsByCategory(userAchievements));
        
        return progress;
    }
    
    private Map<String, List<Achievement>> groupAchievementsByCategory(Set<Achievement> achievements) {
        return achievements.stream()
                .collect(Collectors.groupingBy(Achievement::getCategory));
    }
    
    public int getUserPassportPoints(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return user.getPassportPoints();
    }
    
    public List<Map<String, Object>> getPassportLeaderboard(int limit) {
        // Changed to use findAll and then sort/limit in memory since repository method doesn't exist
        List<User> topUsers = userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getPassportPoints).reversed())
                .limit(limit)
                .collect(Collectors.toList());
                
        return topUsers.stream()
                .map(user -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("id", user.getId());
                    entry.put("username", user.getUsername());
                    entry.put("passportPoints", user.getPassportPoints());
                    entry.put("achievementCount", user.getAchievements().size());
                    return entry;
                })
                .collect(Collectors.toList());
    }
}