package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.exception.ResourceNotFoundException;
import com.lkhn.ecommerce.models.Donation;
import com.lkhn.ecommerce.models.Fundraiser;
import com.lkhn.ecommerce.payload.DonationRequest;
import com.lkhn.ecommerce.payload.FundraiserRequest;
import com.lkhn.ecommerce.payload.MessageResponse;
import com.lkhn.ecommerce.repositories.FundraiserRepository;
import com.lkhn.ecommerce.security.UserDetailsImpl;
import com.lkhn.ecommerce.services.FundraiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fundraisers")
public class FundraiserController {

    @Autowired
    private FundraiserService fundraiserService;

    @GetMapping
    public ResponseEntity<?> getAllFundraisers() {
        List<Fundraiser> fundraisers = fundraiserService.getAllFundraisers();
        return ResponseEntity.ok(fundraisers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFundraiserById(@PathVariable Long id) {
        Fundraiser fundraiser = fundraiserService.getFundraiserById(id);
        return ResponseEntity.ok(fundraiser);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFundraiser(@Valid @RequestBody FundraiserRequest fundraiserRequest) {
        Fundraiser fundraiser = fundraiserService.createFundraiser(
                fundraiserRequest.getTitle(),
                fundraiserRequest.getDescription(),
                fundraiserRequest.getGoalAmount(),
                fundraiserRequest.getBodegaStoreId()
        );
        return ResponseEntity.ok(fundraiser);
    }

    @PostMapping("/{fundraiserId}/donate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> makeDonation(
            @PathVariable Long fundraiserId,
            @Valid @RequestBody DonationRequest donationRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Donation donation = fundraiserService.processDonation(
                fundraiserId,
                userDetails.getId(),
                donationRequest.getAmount(),
                donationRequest.getMessage(),
                donationRequest.isAnonymous()
        );
        return ResponseEntity.ok(donation);
    }

    @PostMapping("/creator")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Fundraiser> createFundraiserByCreator(@RequestBody Map<String, Object> fundraiserData) {
        String title = (String) fundraiserData.get("title");
        String description = (String) fundraiserData.get("description");
        Double goalAmount = Double.valueOf(fundraiserData.get("goalAmount").toString());
        String imageUrl = (String) fundraiserData.get("imageUrl");
        Long creatorId = Long.valueOf(fundraiserData.get("creatorId").toString());

        Fundraiser fundraiser = fundraiserService.createFundraiser(title, description, goalAmount, imageUrl, creatorId);
        return ResponseEntity.ok(fundraiser);
    }


    @GetMapping("/{fundraiserId}/donations")
    public ResponseEntity<?> getFundraiserDonations(@PathVariable Long fundraiserId) {
        List<Donation> donations = fundraiserService.getFundraiserDonations(fundraiserId);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/user/donations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserDonations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Donation> donations = fundraiserService.getUserDonations(userDetails.getId());
        return ResponseEntity.ok(donations);
    }
}
