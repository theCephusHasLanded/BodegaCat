package com.lkhn.ecommerce.services;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BodegaCatService {

    @Autowired
    private BodegaStoreRepository bodegaStoreRepository;
    
    @Autowired
    private CatRepository catRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BodegaVisitRepository visitRepository;
    
    @Autowired
    private AchievementService achievementService;
    
    // BodegaStore methods
    public List<BodegaStore> getAllBodegaStores() {
        return bodegaStoreRepository.findAll();
    }
    
    public Optional<BodegaStore> getBodegaStoreById(Long id) {
        return bodegaStoreRepository.findById(id);
    }
    
    public List<BodegaStore> getBodegaStoresByNeighborhood(String neighborhood) {
        return bodegaStoreRepository.findByNeighborhood(neighborhood);
    }
    
    public List<BodegaStore> searchBodegaStores(String keyword) {
        return bodegaStoreRepository.searchBodegas(keyword);
    }
    
    public List<BodegaStore> getBodegasWithActiveFundraisers() {
        return bodegaStoreRepository.findBodegasWithActiveFundraisers();
    }
    
    @Transactional
    public BodegaStore createBodegaStore(String name, String address, String neighborhood, String description, String ownerName) {
        BodegaStore bodegaStore = new BodegaStore();
        bodegaStore.setName(name);
        bodegaStore.setAddress(address);
        bodegaStore.setNeighborhood(neighborhood);
        bodegaStore.setDescription(description);
        bodegaStore.setOwnerName(ownerName);
        
        return bodegaStoreRepository.save(bodegaStore);
    }
    
    @Transactional
    public BodegaStore updateBodegaStore(Long id, String name, String address, String neighborhood, String description, String ownerName) {
        BodegaStore bodegaStore = bodegaStoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bodega store not found"));
        
        if (name != null) bodegaStore.setName(name);
        if (address != null) bodegaStore.setAddress(address);
        if (neighborhood != null) bodegaStore.setNeighborhood(neighborhood);
        if (description != null) bodegaStore.setDescription(description);
        if (ownerName != null) bodegaStore.setOwnerName(ownerName);
        
        return bodegaStoreRepository.save(bodegaStore);
    }
    
    // Cat methods
    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }
    
    public Optional<Cat> getCatById(Long id) {
        return catRepository.findById(id);
    }
    
    public List<Cat> getCatsByBodegaStore(Long bodegaStoreId) {
        return catRepository.findByBodegaStoreId(bodegaStoreId);
    }
    
    public List<Cat> getFeaturedCats() {
        return catRepository.findByFeatured(true);
    }
    
    public List<Cat> getMostPopularCats() {
        return catRepository.findMostPopularCats();
    }
    
    public List<Cat> getCatsByNeighborhood(String neighborhood) {
        return catRepository.findCatsByNeighborhood(neighborhood);
    }
    
    @Transactional
    public Cat createCat(String name, String breed, String color, String bio, LocalDate dateOfBirth, Long bodegaStoreId) {
        BodegaStore bodegaStore = bodegaStoreRepository.findById(bodegaStoreId)
                .orElseThrow(() -> new RuntimeException("Bodega store not found"));
        
        Cat cat = new Cat();
        cat.setName(name);
        cat.setBreed(breed);
        cat.setColor(color);
        cat.setBio(bio);
        cat.setDateOfBirth(dateOfBirth);
        cat.setDateJoined(LocalDate.now());
        cat.setBodegaStore(bodegaStore);
        
        return catRepository.save(cat);
    }
    
    @Transactional
    public Cat updateCat(Long id, String name, String breed, String color, String bio, String imageUrl, boolean featured) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));
        
        if (name != null) cat.setName(name);
        if (breed != null) cat.setBreed(breed);
        if (color != null) cat.setColor(color);
        if (bio != null) cat.setBio(bio);
        if (imageUrl != null) cat.setImageUrl(imageUrl);
        cat.setFeatured(featured);
        
        return catRepository.save(cat);
    }
    
    @Transactional
    public void likeCat(Long catId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new RuntimeException("Cat not found"));
        
        cat.incrementLikes();
        catRepository.save(cat);
    }
    
    @Transactional
    public void addCatToFavorites(Long userId, Long catId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new RuntimeException("Cat not found"));
        
        user.addFavoriteCat(cat);
        userRepository.save(user);
        
        // Check if user has added 5 cats to favorites to award achievement
        if (user.getFavoriteCats().size() == 5) {
            achievementService.getAchievementsByCategory("SOCIAL").stream()
                .filter(a -> "Cat Lover".equals(a.getName()))
                .findFirst()
                .ifPresent(achievement -> achievementService.grantAchievement(userId, achievement.getId()));
        }
    }
    
    @Transactional
    public void removeCatFromFavorites(Long userId, Long catId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new RuntimeException("Cat not found"));
        
        user.removeFavoriteCat(cat);
        userRepository.save(user);
    }
    
    // Visit methods
    public List<BodegaVisit> getAllVisits() {
        return visitRepository.findAll();
    }
    
    public Optional<BodegaVisit> getVisitById(Long id) {
        return visitRepository.findById(id);
    }
    
    public List<BodegaVisit> getVisitsByUser(Long userId) {
        return visitRepository.findByUserId(userId);
    }
    
    public List<BodegaVisit> getVisitsByBodegaStore(Long bodegaStoreId) {
        return visitRepository.findByBodegaStoreId(bodegaStoreId);
    }
    
    @Transactional
    public BodegaVisit recordVisit(Long userId, Long bodegaStoreId, String comment, Integer rating, String photoUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        BodegaStore bodegaStore = bodegaStoreRepository.findById(bodegaStoreId)
                .orElseThrow(() -> new RuntimeException("Bodega store not found"));
        
        BodegaVisit visit = new BodegaVisit();
        visit.setUser(user);
        visit.setBodegaStore(bodegaStore);
        visit.setVisitDate(LocalDateTime.now());
        visit.setComment(comment);
        visit.setRating(rating);
        visit.setPhotoUrl(photoUrl);
        
        BodegaVisit savedVisit = visitRepository.save(visit);
        
        // Check for visit-based achievements
        int visitCount = visitRepository.countVisitsByUserAndBodega(userId, bodegaStoreId);
        
        if (visitCount == 1) {
            // This is their first time visiting this bodega
            int totalUniqueVisits = (int) visitRepository.findByUserId(userId).stream()
                    .map(v -> v.getBodegaStore().getId())
                    .distinct()
                    .count();
            
            achievementService.grantVisitAchievement(userId, totalUniqueVisits);
        }
        
        return savedVisit;
    }
    
    @Transactional
    public void verifyVisit(Long visitId) {
        BodegaVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        
        visit.setVerified(true);
        visitRepository.save(visit);
    }
    
    public List<Object[]> getMostVisitedBodegas() {
        return visitRepository.findMostVisitedBodegas();
    }
}