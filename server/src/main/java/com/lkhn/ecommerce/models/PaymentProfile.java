package com.lkhn.ecommerce.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_profiles")
public class PaymentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    // Tokenized payment methods
    private String stripeCustomerId;

    // Benefits-related fields
    private String ebtTokenId;
    private boolean ebtVerified;

    @ElementCollection
    @CollectionTable(name = "payment_profile_benefit_programs",
                    joinColumns = @JoinColumn(name = "payment_profile_id"))
    @Column(name = "program_code")
    private Set<String> enrolledBenefitPrograms = new HashSet<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getEbtTokenId() {
        return ebtTokenId;
    }

    public void setEbtTokenId(String ebtTokenId) {
        this.ebtTokenId = ebtTokenId;
    }

    public boolean isEbtVerified() {
        return ebtVerified;
    }

    public void setEbtVerified(boolean ebtVerified) {
        this.ebtVerified = ebtVerified;
    }

    public Set<String> getEnrolledBenefitPrograms() {
        return enrolledBenefitPrograms;
    }

    public void setEnrolledBenefitPrograms(Set<String> enrolledBenefitPrograms) {
        this.enrolledBenefitPrograms = enrolledBenefitPrograms;
    }
}
