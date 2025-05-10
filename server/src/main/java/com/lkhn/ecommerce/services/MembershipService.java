package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.models.Membership;
import com.lkhn.ecommerce.models.User;
import com.lkhn.ecommerce.repositories.MembershipRepository;
import com.lkhn.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private UserRepository userRepository;

    public static final String CASUAL_TIER = "CASUAL";
    public static final String REGULAR_TIER = "REGULAR";
    public static final String CHAMPION_TIER = "CHAMPION";

    public static final double REGULAR_PRICE = 5.00;
    public static final double CHAMPION_PRICE = 15.00;

    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    public Optional<Membership> getMembershipById(Long id) {
        return membershipRepository.findById(id);
    }

    public List<Membership> getMembershipsByUser(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    public Optional<Membership> getActiveMembershipForUser(Long userId) {
        return membershipRepository.findByUserIdAndActive(userId, true);
    }

    @Transactional
    public Membership createCasualMembership(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cancel any existing active membership
        cancelActiveSubscription(userId);

        Membership membership = new Membership();
        membership.setTier(CASUAL_TIER);
        membership.setMonthlyFee(0.0);
        membership.setStartDate(LocalDateTime.now());
        membership.setUser(user);
        membership.setActive(true);

        return membershipRepository.save(membership);
    }

    @Transactional
    public Membership createRegularMembership(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cancel any existing active membership
        cancelActiveSubscription(userId);

        Membership membership = new Membership();
        membership.setTier(REGULAR_TIER);
        membership.setMonthlyFee(REGULAR_PRICE);
        membership.setStartDate(LocalDateTime.now());
        membership.setUser(user);
        membership.setActive(true);

        return membershipRepository.save(membership);
    }

    @Transactional
    public Membership createChampionMembership(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cancel any existing active membership
        cancelActiveSubscription(userId);

        Membership membership = new Membership();
        membership.setTier(CHAMPION_TIER);
        membership.setMonthlyFee(CHAMPION_PRICE);
        membership.setStartDate(LocalDateTime.now());
        membership.setUser(user);
        membership.setActive(true);

        return membershipRepository.save(membership);
    }

    @Transactional
    public void cancelMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        membership.setActive(false);
        membership.setEndDate(LocalDateTime.now());

        membershipRepository.save(membership);
    }

    @Transactional
    public void cancelActiveSubscription(Long userId) {
        Optional<Membership> activeMembership = membershipRepository.findByUserIdAndActive(userId, true);

        activeMembership.ifPresent(membership -> {
            membership.setActive(false);
            membership.setEndDate(LocalDateTime.now());
            membershipRepository.save(membership);
        });
    }

    public long countActiveMembersByTier(String tier) {
        return membershipRepository.countActiveMembershipsByTier(tier);
    }

    public List<Membership> getAllActiveMemberships() {
        return membershipRepository.findAllActiveMemberships();
    }

    public boolean isUserPremiumMember(Long userId) {
        Optional<Membership> membership = membershipRepository.findByUserIdAndActive(userId, true);
        return membership.isPresent() &&
               (REGULAR_TIER.equals(membership.get().getTier()) ||
                CHAMPION_TIER.equals(membership.get().getTier()));
    }

    public void cancelActiveMembership(Long userId) {
        // Implementation goes here
        membershipRepository.findByUserIdAndActive(userId, true).ifPresent(membership -> {
            membership.setActive(false);
            membership.setEndDate(LocalDateTime.now());
            membershipRepository.save(membership);
        });
    }

    public Membership getCurrentMembership(Long userId) {
        // Implementation goes here
        return membershipRepository.findByUserIdAndActive(userId, true)
            .orElse(null);
    }

    // Removed MembershipTierBenefit method as the class doesn't exist yet
    // public List<MembershipTierBenefit> getAllMembershipTierBenefits() {
    //     // Implementation goes here
    //     return membershipTierBenefitRepository.findAll();
    // }


    public boolean isUserChampionMember(Long userId) {
        Optional<Membership> membership = membershipRepository.findByUserIdAndActive(userId, true);
        return membership.isPresent() && CHAMPION_TIER.equals(membership.get().getTier());
    }
}
