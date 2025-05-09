package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.models.BodegaStore;
import com.lkhn.ecommerce.models.BodegaVisit;
import com.lkhn.ecommerce.models.Cat;
import com.lkhn.ecommerce.exception.ResourceNotFoundException;
import com.lkhn.ecommerce.payload.BodegaStoreRequest;
import com.lkhn.ecommerce.payload.BodegaVisitRequest;
import com.lkhn.ecommerce.payload.CatRequest;
import com.lkhn.ecommerce.payload.MessageResponse;
import com.lkhn.ecommerce.security.UserDetailsImpl;
import com.lkhn.ecommerce.services.BodegaCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bodegas")
public class BodegaController {

    @Autowired
    private BodegaCatService bodegaCatService;

    @GetMapping
    public ResponseEntity<?> getAllBodegaStores() {
        List<BodegaStore> stores = bodegaCatService.getAllBodegaStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBodegaStoreById(@PathVariable Long id) {
        BodegaStore store = bodegaCatService.getBodegaStoreById(id);
        return ResponseEntity.ok(store);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBodegaStore(@Valid @RequestBody BodegaStoreRequest storeRequest) {
        BodegaStore store = bodegaCatService.createBodegaStore(
                storeRequest.getName(),
                storeRequest.getAddress(),
                storeRequest.getNeighborhood(),
                storeRequest.getDescription(),
                storeRequest.getImageUrl()
        );
        return ResponseEntity.ok(store);
    }

    @GetMapping("/{storeId}/cats")
    public ResponseEntity<?> getBodegaStoreCats(@PathVariable Long storeId) {
        List<Cat> cats = bodegaCatService.getCatsByBodegaStore(storeId);
        return ResponseEntity.ok(cats);
    }

    @PostMapping("/{storeId}/cats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCatToBodegaStore(
            @PathVariable Long storeId,
            @Valid @RequestBody CatRequest catRequest
    ) {
        Cat cat = bodegaCatService.addCatToBodegaStore(
                storeId,
                catRequest.getName(),
                catRequest.getDescription(),
                catRequest.getImageUrl()
        );
        return ResponseEntity.ok(cat);
    }

    @PostMapping("/{storeId}/visits")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> recordBodegaVisit(
            @PathVariable Long storeId,
            @Valid @RequestBody BodegaVisitRequest visitRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BodegaVisit visit = bodegaCatService.recordBodegaVisit(
                userDetails.getId(),
                storeId,
                visitRequest.getVisitDate(),
                visitRequest.getRating(),
                visitRequest.getReview(),
                visitRequest.getPhotoUrl()
        );
        return ResponseEntity.ok(visit);
    }

    @GetMapping("/{storeId}/visits")
    public ResponseEntity<?> getBodegaVisits(@PathVariable Long storeId) {
        List<BodegaVisit> visits = bodegaCatService.getBodegaVisitsByStore(storeId);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/user/visits")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserBodegaVisits(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BodegaVisit> visits = bodegaCatService.getBodegaVisitsByUser(userDetails.getId());
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/neighborhood/{neighborhood}")
    public ResponseEntity<?> getBodegaStoresByNeighborhood(@PathVariable String neighborhood) {
        List<BodegaStore> stores = bodegaCatService.getBodegaStoresByNeighborhood(neighborhood);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/neighborhoods")
    public ResponseEntity<?> getAllNeighborhoods() {
        List<String> neighborhoods = bodegaCatService.getAllNeighborhoods();
        return ResponseEntity.ok(neighborhoods);
    }
}
