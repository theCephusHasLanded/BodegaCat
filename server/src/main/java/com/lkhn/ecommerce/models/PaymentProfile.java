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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "default_payment_method_id")
    private String defaultPaymentMethodId;

    @Column(name = "ebt_verified")
    private boolean ebtVerified;

    @Column(name = "ebt_token_id")
    private String ebtTokenId;

    @ElementCollection
    @CollectionTable(name = "benefit_programs", joinColumns = @JoinColumn(name = "payment_profile_id"))
    @Column(name = "program_name")
    private Set<String> enrolledBenefitPrograms = new HashSet<>();

    // Constructors
    public PaymentProfile() {
    }

    public PaymentProfile(Long userId) {
        this.userId = userId;
    }

    // Getters and Setters
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

    public String getDefaultPaymentMethodId() {
        return defaultPaymentMethodId;
    }

    public void setDefaultPaymentMethodId(String defaultPaymentMethodId) {
        this.defaultPaymentMethodId = defaultPaymentMethodId;
    }

    public boolean isEbtVerified() {
        return ebtVerified;
    }

    public void setEbtVerified(boolean ebtVerified) {
        this.ebtVerified = ebtVerified;
    }

    public String getEbtTokenId() {
        return ebtTokenId;
    }

    public void setEbtTokenId(String ebtTokenId) {
        this.ebtTokenId = ebtTokenId;
    }

    public Set<String> getEnrolledBenefitPrograms() {
        return enrolledBenefitPrograms;
    }

    public void setEnrolledBenefitPrograms(Set<String> enrolledBenefitPrograms) {
        this.enrolledBenefitPrograms = enrolledBenefitPrograms;
    }

    public void addBenefitProgram(String programName) {
        this.enrolledBenefitPrograms.add(programName);
    }

    public void removeBenefitProgram(String programName) {
        this.enrolledBenefitPrograms.remove(programName);
    }

    @Override
    public String toString() {
        return "PaymentProfile{" +
                "id=" + id +
                ", userId=" + userId +
                ", stripeCustomerId='" + stripeCustomerId + '\'' +
                ", defaultPaymentMethodId='" + defaultPaymentMethodId + '\'' +
                ", ebtVerified=" + ebtVerified +
                '}';
    }
}