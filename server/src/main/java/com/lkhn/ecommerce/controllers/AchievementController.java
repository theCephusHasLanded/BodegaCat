package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.models.Achievement;
import com.lkhn.ecommerce.security.UserDetailsImpl;
import com.lkhn.ecommerce.services.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping
    public ResponseEntity<?> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAchievementCategories() {
        return ResponseEntity.ok(achievementService.getAllAchievementCategories());
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserAchievements(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Achievement> achievements = achievementService.getUserAchievements(userDetails.getId());
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/user/progress")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserAchievementProgress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> progress = achievementService.getUserAchievementProgress(userDetails.getId());
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/user/points")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserPassportPoints(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        int points = achievementService.getUserPassportPoints(userDetails.getId());
        return ResponseEntity.ok(Map.of("passportPoints", points));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getAchievementLeaderboard() {
        return ResponseEntity.ok(achievementService.getPassportLeaderboard(10));
    }
}