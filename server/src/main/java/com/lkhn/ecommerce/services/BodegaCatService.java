package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.exception.ResourceNotFoundException;
import com.lkhn.ecommerce.models.BodegaStore;
import com.lkhn.ecommerce.models.BodegaVisit;
import com.lkhn.ecommerce.models.Cat;
import com.lkhn.ecommerce.models.User;
import com.lkhn.ecommerce.repositories.BodegaStoreRepository;
import com.lkhn.ecommerce.repositories.BodegaVisitRepository;
import com.lkhn.ecommerce.repositories.CatRepository;
import com.lkhn.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BodegaCatService {

    @Autowired
    private BodegaStoreRepository bodegaStoreRepository;
    
    @Autowired
    private CatRepository catRepository;
    
    @Autowired
    private BodegaVisitRepository bodegaVisitRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AchievementService achievementService;

    public List<BodegaStore> getAllBodegaStores() {
        return bodegaStoreRepository.findAll();
    }

    public BodegaStore getBodegaStoreById(Long id) {
        return bodegaStoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BodegaStore not found with id: " + id));
    }

    @Transactional
    public BodegaStore createBodegaStore(String name, String address, String neighborhood, String description, String imageUrl) {
        BodegaStore store = new BodegaStore();
        store.setName(name);
        store.setAddress(address);
        store.setNeighborhood(neighborhood);
        store.setDescription(description);
        store.setImageUrl(imageUrl);
        
        return bodegaStoreRepository.save(store);
    }

    public List<Cat> getCatsByBodegaStore(Long storeId) {
        return catRepository.findByBodegaStoreId(storeId);
    }

    @Transactional
    public Cat addCatToBodegaStore(Long storeId, String name, String description, String imageUrl) {
        BodegaStore store = getBodegaStoreById(storeId);
        
        Cat cat = new Cat();
        cat.setName(name);
        cat.setDescription(description);
        cat.setImageUrl(imageUrl);
        cat.setBodegaStore(store);
        
        return catRepository.save(cat);
    }

    @Transactional
    public BodegaVisit recordBodegaVisit(Long userId, Long storeId, LocalDateTime visitDate, Integer rating, String review, String photoUrl) {
        BodegaStore store = getBodegaStoreById(storeId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        BodegaVisit visit = new BodegaVisit();
        visit.setBodegaStore(store);
        visit.setUser(user);
        visit.setVisitDate(visitDate != null ? visitDate : LocalDateTime.now());
        visit.setRating(rating);
        visit.setReview(review);
        visit.setPhotoUrl(photoUrl);
        
        BodegaVisit savedVisit = bodegaVisitRepository.save(visit);
        
        // Check if visit unlocks achievements
        checkVisitAchievements(user, store);
        
        return savedVisit;
    }

    private void checkVisitAchievements(User user, BodegaStore store) {
        // First visit achievement
        achievementService.checkAndGrantAchievement(user.getId(), "FIRST_VISIT");
        
        // Count total visits
        long visitCount = bodegaVisitRepository.countByUserId(user.getId());
        
        // Visit count achievements
        if (visitCount >= 5) {
            achievementService.checkAndGrantAchievement(user.getId(), "REGULAR_VISITOR");
        }
        
        if (visitCount >= 20) {
            achievementService.checkAndGrantAchievement(user.getId(), "BODEGA_EXPLORER");
        }
        
        if (visitCount >= 50) {
            achievementService.checkAndGrantAchievement(user.getId(), "BODEGA_ENTHUSIAST");
        }
        
        // Neighborhood visits - count unique neighborhoods visited
        List<BodegaVisit> userVisits = bodegaVisitRepository.findByUserId(user.getId());
        long uniqueNeighborhoods = userVisits.stream()
                .map(visit -> visit.getBodegaStore().getNeighborhood())
                .distinct()
                .count();
        
        if (uniqueNeighborhoods >= 3) {
            achievementService.checkAndGrantAchievement(user.getId(), "NEIGHBORHOOD_EXPLORER");
        }
        
        if (uniqueNeighborhoods >= 10) {
            achievementService.checkAndGrantAchievement(user.getId(), "CITY_EXPLORER");
        }
    }

    public List<BodegaVisit> getBodegaVisitsByStore(Long storeId) {
        return bodegaVisitRepository.findByBodegaStoreId(storeId);
    }

    public List<BodegaVisit> getBodegaVisitsByUser(Long userId) {
        return bodegaVisitRepository.findByUserId(userId);
    }

    public List<BodegaStore> getBodegaStoresByNeighborhood(String neighborhood) {
        return bodegaStoreRepository.findByNeighborhood(neighborhood);
    }

    public List<String> getAllNeighborhoods() {
        return bodegaStoreRepository.findAll().stream()
                .map(BodegaStore::getNeighborhood)
                .distinct()
                .collect(Collectors.toList());
    }
}