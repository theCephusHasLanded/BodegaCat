package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByCategory(String category);
    
    @Query("SELECT a FROM Achievement a JOIN a.users u WHERE u.id = ?1")
    Set<Achievement> findAchievementsByUser(Long userId);
    
    @Query("SELECT a FROM Achievement a WHERE a.id NOT IN (SELECT a.id FROM Achievement a JOIN a.users u WHERE u.id = ?1)")
    Set<Achievement> findAchievementsNotEarnedByUser(Long userId);
    
    @Query("SELECT a FROM Achievement a ORDER BY SIZE(a.users) DESC")
    List<Achievement> findMostEarnedAchievements();
}