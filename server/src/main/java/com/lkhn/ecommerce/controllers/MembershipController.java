package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.models.Membership;
import com.lkhn.ecommerce.models.User;
import com.lkhn.ecommerce.payload.MessageResponse;
import com.lkhn.ecommerce.security.UserDetailsImpl;
import com.lkhn.ecommerce.services.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Collections;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @PostMapping("/subscribe/regular")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> subscribeRegular(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Membership membership = membershipService.createRegularMembership(userDetails.getId());
        return ResponseEntity.ok(membership);
    }

    @PostMapping("/subscribe/champion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> subscribeChampion(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Membership membership = membershipService.createChampionMembership(userDetails.getId());
        return ResponseEntity.ok(membership);
    }

    @DeleteMapping("/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelMembership(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        membershipService.cancelActiveMembership(userDetails.getId());
        return ResponseEntity.ok(new MessageResponse("Membership successfully cancelled"));
    }

    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentMembership(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Membership membership = membershipService.getCurrentMembership(userDetails.getId());
        if (membership == null) {
            return ResponseEntity.ok(new MessageResponse("No active membership found"));
        }
        return ResponseEntity.ok(membership);
    }

    @GetMapping("/benefits")
    public ResponseEntity<?> getMembershipBenefits() {
        // Temporarily return empty list until MembershipTierBenefit is implemented
        return ResponseEntity.ok(Collections.emptyList());
    }
}