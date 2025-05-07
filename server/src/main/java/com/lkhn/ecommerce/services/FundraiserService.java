package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.models.BodegaStore;
import com.lkhn.ecommerce.models.Donation;
import com.lkhn.ecommerce.models.Fundraiser;
import com.lkhn.ecommerce.models.User;
import com.lkhn.ecommerce.repositories.BodegaStoreRepository;
import com.lkhn.ecommerce.repositories.DonationRepository;
import com.lkhn.ecommerce.repositories.FundraiserRepository;
import com.lkhn.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FundraiserService {

    @Autowired
    private FundraiserRepository fundraiserRepository;
    
    @Autowired
    private BodegaStoreRepository bodegaStoreRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private AchievementService achievementService;
    
    public List<Fundraiser> getAllFundraisers() {
        return fundraiserRepository.findAll();
    }
    
    public Optional<Fundraiser> getFundraiserById(Long id) {
        return fundraiserRepository.findById(id);
    }
    
    public List<Fundraiser> getFundraisersByBodegaStore(Long bodegaStoreId) {
        return fundraiserRepository.findByBodegaStoreId(bodegaStoreId);
    }
    
    public List<Fundraiser> getActiveFundraisersByBodegaStore(Long bodegaStoreId) {
        return fundraiserRepository.findByActiveAndBodegaStoreId(true, bodegaStoreId);
    }
    
    public List<Fundraiser> getFundraisersByCategory(String category) {
        return fundraiserRepository.findByCategory(category);
    }
    
    public List<Fundraiser> getTopProgressFundraisers() {
        return fundraiserRepository.findTopProgressFundraisers();
    }
    
    public List<Fundraiser> getMostSupportedFundraisers() {
        return fundraiserRepository.findMostSupportedFundraisers();
    }
    
    @Transactional
    public Fundraiser createFundraiser(String title, String description, Double goalAmount, 
                                      String category, Long bodegaStoreId) {
        BodegaStore bodegaStore = bodegaStoreRepository.findById(bodegaStoreId)
                .orElseThrow(() -> new RuntimeException("Bodega store not found"));
        
        Fundraiser fundraiser = new Fundraiser();
        fundraiser.setTitle(title);
        fundraiser.setDescription(description);
        fundraiser.setGoalAmount(goalAmount);
        fundraiser.setStartDate(LocalDateTime.now());
        fundraiser.setCategory(category);
        fundraiser.setBodegaStore(bodegaStore);
        fundraiser.setActive(true);
        
        return fundraiserRepository.save(fundraiser);
    }
    
    @Transactional
    public void closeFundraiser(Long fundraiserId) {
        Fundraiser fundraiser = fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new RuntimeException("Fundraiser not found"));
        
        fundraiser.setActive(false);
        fundraiser.setEndDate(LocalDateTime.now());
        
        fundraiserRepository.save(fundraiser);
    }
    
    @Transactional
    public Donation makeDonation(Long fundraiserId, Long userId, Double amount, String message, boolean anonymous) {
        Fundraiser fundraiser = fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new RuntimeException("Fundraiser not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!fundraiser.isActive()) {
            throw new RuntimeException("This fundraiser is no longer active");
        }
        
        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setUser(user);
        donation.setFundraiser(fundraiser);
        donation.setMessage(message);
        donation.setAnonymous(anonymous);
        donation.setDonationDate(LocalDateTime.now());
        
        // Add donation to fundraiser and update current amount
        fundraiser.addDonation(donation);
        
        // Check if donation qualifies for achievements
        checkForDonationAchievements(user, amount);
        
        return donationRepository.save(donation);
    }
    
    private void checkForDonationAchievements(User user, Double amount) {
        // Check for first donation achievement
        Double totalDonations = donationRepository.getTotalDonationsByUser(user.getId());
        if (totalDonations == null || totalDonations <= amount) {
            // This is the first donation
            achievementService.grantFirstDonationAchievement(user.getId());
        }
        
        // Check for donation amount milestones
        totalDonations = (totalDonations == null ? 0 : totalDonations) + amount;
        
        if (totalDonations >= 100) {
            achievementService.grantDonationMilestoneAchievement(user.getId(), "SUPPORTER");
        }
        
        if (totalDonations >= 500) {
            achievementService.grantDonationMilestoneAchievement(user.getId(), "PATRON");
        }
        
        if (totalDonations >= 1000) {
            achievementService.grantDonationMilestoneAchievement(user.getId(), "BENEFACTOR");
        }
    }
    
    public Double getTotalDonationsForFundraiser(Long fundraiserId) {
        return donationRepository.getTotalDonationsForFundraiser(fundraiserId);
    }
    
    public List<Donation> getDonationsByFundraiser(Long fundraiserId) {
        return donationRepository.findByFundraiserId(fundraiserId);
    }
    
    public List<Donation> getDonationsByUser(Long userId) {
        return donationRepository.findByUserId(userId);
    }
    
    public Double getTotalDonationsByUser(Long userId) {
        return donationRepository.getTotalDonationsByUser(userId);
    }
}