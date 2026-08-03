package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.IdentityVerification;
import com.irsyad.pulse.orchestrator.domain.enums.DukcapilStatus;
import com.irsyad.pulse.orchestrator.domain.enums.KycStatus;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.integration.customer.CustomerService;
import com.irsyad.pulse.orchestrator.integration.dukcapil.DukcapilService;
import com.irsyad.pulse.orchestrator.integration.liveness.LivenessService;
import com.irsyad.pulse.orchestrator.integration.policy.PolicyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Delegate for validating customer identity before processing
 * This performs identity verification checks using Dukcapil and KYC status
 */
@ApplicationScoped
public class ValidateIdentityDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateIdentityDelegate.class);

    @Inject
    DukcapilService dukcapilService;

    @Inject
    CustomerService customerService;

    @Inject
    LivenessService livenessService;

    @Inject
    PolicyService policyService;

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        // Extract request from model
        CheckoutRequest request = model.getRequest();

        // If request is null, try to reconstruct from model fields
        if (request == null) {
            LOG.warn("CheckoutRequest is null in process model, reconstructing from model fields");
            request = new CheckoutRequest();
            request.setOrderId(model.getCheckoutId());
            request.setCustomerId(model.getCustomerId());
        }

        LOG.info("Validating identity for customer {}", request.getCustomerId());

        IdentityVerification identity = new IdentityVerification();

        try {
            validateIdentity(request, model, identity);

            identity.setIdentityVerified(true);

        } catch (Exception ex) {
            identity.setIdentityVerified(false);
            throw ex;
        }

        model.setIdentity(identity);
        return model;
    }

    private void validateIdentity(CheckoutRequest request, CheckoutProcessModel model, IdentityVerification identity) {
        // Step 1: Get customer data
        CustomerService.CustomerInfo customerInfo = customerService.getPolicyholderInfo(request.getCustomerId());

        // Step 2: Verify identity via Dukcapil
        String nik = request.getNik() != null ? request.getNik() : customerInfo.getNik();
        DukcapilService.DukcapilInfo dukcapilInfo = dukcapilService.verifyIdentity(nik);

        // Step 3: Check liveness (tanpa face matching)
        boolean livenessPassed = checkLiveness(request);

        // Step 4: Calculate age from dateOfBirth
        Integer age = calculateAge(request.getDateOfBirth());

        // Step 5: Map occupation to class
        String occupationClass = mapOccupationToClass(request.getOccupation());

        // Step 6: Query existing policy for total active UP
        BigDecimal totalActiveUP = queryTotalActiveUP(request.getCustomerId());

        // Step 7: Calculate confidence score (eKYC aggregation)
        Integer confidenceScore = calculateConfidenceScore(dukcapilInfo, livenessPassed);

        // Step 8: Determine identity status
        String identityStatus = determineIdentityStatus(dukcapilInfo, confidenceScore);

        // Set all fields
        identity.setNik(nik);
        identity.setFullName(request.getFullName());
        identity.setDateOfBirth(request.getDateOfBirth());
        identity.setOccupation(request.getOccupation());
        identity.setAge(age);
        identity.setOccupationClass(occupationClass);
        identity.setExistingActiveSumInsured(totalActiveUP);
        identity.setConfidenceScore(confidenceScore);
        identity.setIdentityStatus(identityStatus);
        identity.setDukcapilStatus(dukcapilInfo.getDukcapilStatus());
        identity.setKycStatus(model.getKycStatus() != null ? model.getKycStatus().name() : KycStatus.PENDING.name());
        identity.setReasonCode(dukcapilInfo.getStatus());
        identity.setMessage("Identity verification completed");
        identity.setVerifiedAt(java.time.Instant.now());
        identity.setIdentityVerified(true);

        // Set model fields
        model.setNik(nik);
        model.setDukcapilStatus(DukcapilStatus.valueOf(dukcapilInfo.getDukcapilStatus()));
        model.setKycStatus(model.getKycStatus() != null ? model.getKycStatus() : KycStatus.PENDING);

        // Validate and throw exception if failed
        validateIdentityResult(dukcapilInfo, identityStatus, model.getKycStatus());
    }

    private boolean checkLiveness(CheckoutRequest request) {
        try {
            LOG.info("Checking liveness for customer: {}", request.getCustomerId());
            boolean livenessPassed = livenessService.checkLiveness(request.getCustomerId());
            LOG.info("Liveness check result for {}: {}", request.getCustomerId(), livenessPassed);
            return livenessPassed;
        } catch (Exception e) {
            LOG.warn("Liveness check failed: {}", e.getMessage());
            return false;
        }
    }

    private Integer calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isBlank()) {
            LOG.warn("Date of birth is null, cannot calculate age");
            return null;
        }

        try {
            // Parse dateOfBirth (format: YYYY-MM-DD or OffsetDateTime)
            java.time.LocalDate birthDate = java.time.LocalDate.parse(dateOfBirth);
            return java.time.LocalDate.now().getYear() - birthDate.getYear();
        } catch (Exception e) {
            LOG.error("Failed to calculate age from dateOfBirth: {}", dateOfBirth, e);
            return null;
        }
    }

    private String mapOccupationToClass(String occupation) {
        if (occupation == null || occupation.isBlank()) {
            return "UNKNOWN";
        }

        // Map occupation to class based on risk
        return switch (occupation.toUpperCase()) {
            case "GURU", "DOSEN", "DOKTER", "PERAWAT" -> "CLASS_1";
            case "KARYAWAN", "PEGAWAI", "SWASTA" -> "CLASS_2";
            case "SUPIR", "SOPIR", "TRANSPORTASI" -> "CLASS_3";
            case "TAMBANG", "KONSTRUKSI", "PERTAMBANGAN" -> "CLASS_4";
            default -> "CLASS_2"; // Default
        };
    }

    private BigDecimal queryTotalActiveUP(String customerId) {
        try {
            LOG.info("Querying total active UP for customer: {}", customerId);

            PolicyService.PolicyInfo policy = policyService.getTotalActiveUP(customerId);

            if (policy == null || policy.getTotalActiveUP() == null) {
                LOG.warn("No policy data found for customer: {}, returning zero", customerId);
                return BigDecimal.ZERO;
            }

            LOG.info("Total active UP for customer {}: {}", customerId, policy.getTotalActiveUP());
            return policy.getTotalActiveUP();

        } catch (Exception e) {
            LOG.error("Failed to query total active UP for customer: {}", customerId, e);
            return BigDecimal.ZERO;
        }
    }

    private Integer calculateConfidenceScore(DukcapilService.DukcapilInfo dukcapilInfo, boolean livenessPassed) {
        // eKYC Aggregation: combine Dukcapil score + Liveness score
        int dukcapilScore = "MATCH".equals(dukcapilInfo.getStatus()) ? 90 : 50;
        int livenessScore = livenessPassed ? 95 : 40;

        // Weighted average: Dukcapil 60%, Liveness 40%
        int confidenceScore = (int) (dukcapilScore * 0.6 + livenessScore * 0.4);

        LOG.info("Confidence score calculated: {} (dukcapil: {}, liveness: {})",
                confidenceScore, dukcapilScore, livenessScore);

        return confidenceScore;
    }

    private String determineIdentityStatus(DukcapilService.DukcapilInfo dukcapilInfo, Integer confidenceScore) {
        if (confidenceScore == null) {
            return "UNKNOWN";
        }

        if (confidenceScore >= 80) {
            return "VERIFIED";
        } else if (confidenceScore >= 50) {
            return "REVIEW";
        } else {
            return "UNVERIFIED";
        }
    }

    private void validateIdentityResult(DukcapilService.DukcapilInfo dukcapilInfo, String identityStatus, KycStatus kycStatus) {
        // Validate Dukcapil match
        if (dukcapilInfo.getStatus() == null || !"MATCH".equals(dukcapilInfo.getStatus())) {
            throw new IllegalArgumentException("Identity verification failed: Dukcapil status is " + dukcapilInfo.getStatus());
        }

        if (dukcapilInfo.getDukcapilStatus() == null || !"VALID".equals(dukcapilInfo.getDukcapilStatus())) {
            throw new IllegalArgumentException("Identity verification failed: Dukcapil record is " + dukcapilInfo.getDukcapilStatus());
        }

        if (kycStatus == KycStatus.FAILED || kycStatus == KycStatus.REJECTED) {
            throw new IllegalArgumentException("Identity verification failed: KYC status is " + kycStatus);
        }

        if ("UNVERIFIED".equals(identityStatus)) {
            throw new IllegalArgumentException("Identity verification failed: low confidence score");
        }
    }
}