package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.exception.ResourceNotFoundException;
import com.lkhn.ecommerce.models.Donation;
import com.lkhn.ecommerce.models.Fundraiser;
import com.lkhn.ecommerce.models.User;
import com.lkhn.ecommerce.repositories.DonationRepository;
import com.lkhn.ecommerce.repositories.FundraiserRepository;
import com.lkhn.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FundraiserService {

    @Autowired
    private FundraiserRepository fundraiserRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AchievementService achievementService;

    public List<Fundraiser> getAllFundraisers() {
        return fundraiserRepository.findAll();
    }

    public Fundraiser getFundraiserById(Long id) {
        return fundraiserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fundraiser not found with id: " + id));
    }

    @Transactional
    public Fundraiser createFundraiser(String title, String description, Double goalAmount, Long bodegaStoreId) {
        Fundraiser fundraiser = new Fundraiser();
        fundraiser.setTitle(title);
        fundraiser.setDescription(description);
        fundraiser.setGoalAmount(goalAmount);
        fundraiser.setCurrentAmount(0.0);
        fundraiser.setCreationDate(LocalDateTime.now());
        
        // Set bodega store reference if provided
        if (bodegaStoreId != null) {
            // Add BodegaStore reference logic here
        }
        
        return fundraiserRepository.save(fundraiser);
    }

    @Transactional
    public Fundraiser createFundraiser(String title, String description, Double goalAmount, String imageUrl, Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorId));
        
        Fundraiser fundraiser = new Fundraiser();
        fundraiser.setTitle(title);
        fundraiser.setDescription(description);
        fundraiser.setGoalAmount(goalAmount);
        fundraiser.setCurrentAmount(0.0);
        fundraiser.setCreationDate(LocalDateTime.now());
        fundraiser.setImageUrl(imageUrl);
        fundraiser.setCreator(creator);
        
        return fundraiserRepository.save(fundraiser);
    }

    @Transactional
    public Donation processDonation(Long fundraiserId, Long userId, Double amount, String message, boolean anonymous) {
        Fundraiser fundraiser = getFundraiserById(fundraiserId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Create and save donation
        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setMessage(message);
        donation.setAnonymous(anonymous);
        donation.setDonationDate(LocalDateTime.now());
        donation.setFundraiser(fundraiser);
        donation.setUser(user);
        
        // Update fundraiser amount
        fundraiser.setCurrentAmount(fundraiser.getCurrentAmount() + amount);
        fundraiserRepository.save(fundraiser);
        
        // Check if donation unlocks achievements
        checkDonationAchievements(user, amount);
        
        return donationRepository.save(donation);
    }

    private void checkDonationAchievements(User user, Double amount) {
        // First donation achievement
        achievementService.checkAndGrantAchievement(user.getId(), "FIRST_DONATION");
        
        // Donation amount-based achievements
        if (amount >= 5.0) {
            achievementService.checkAndGrantAchievement(user.getId(), "SUPPORTER");
        }
        
        if (amount >= 25.0) {
            achievementService.checkAndGrantAchievement(user.getId(), "BENEFACTOR");
        }
        
        if (amount >= 100.0) {
            achievementService.checkAndGrantAchievement(user.getId(), "CHAMPION");
        }
        
        // Cumulative donations achievements
        Double totalDonated = getTotalUserDonations(user.getId());
        
        if (totalDonated >= 50.0) {
            achievementService.checkAndGrantAchievement(user.getId(), "REGULAR_SUPPORTER");
        }
        
        if (totalDonated >= 250.0) {
            achievementService.checkAndGrantAchievement(user.getId(), "COMMUNITY_PILLAR");
        }
    }

    private Double getTotalUserDonations(Long userId) {
        List<Donation> userDonations = donationRepository.findByUserId(userId);
        return userDonations.stream()
                .mapToDouble(Donation::getAmount)
                .sum();
    }

    public List<Donation> getFundraiserDonations(Long fundraiserId) {
        return donationRepository.findByFundraiserId(fundraiserId);
    }

    public List<Donation> getUserDonations(Long userId) {
        return donationRepository.findByUserId(userId);
    }
}