package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.event.CheckoutCompletedEvent;
import com.irsyad.pulse.engine.persistence.entity.CustomerLearningEntity;
import com.irsyad.pulse.engine.persistence.repository.CustomerLearningRepository;
import com.irsyad.pulse.engine.pipeline.UnderstandingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.*;

@ApplicationScoped
public class UnderstandingService {

    @Inject
    CustomerLearningRepository customerLearningRepository;

    public UnderstandingContext understand(CheckoutCompletedEvent event) {
        // 1. Get customer learning data
        CustomerLearningEntity learning = customerLearningRepository.findById(event.getCustomerId());
        boolean isFirstPurchase = learning == null;

        // 2. Determine amount threshold (10 juta)
        boolean highAmount = event.getAmount().compareTo(BigDecimal.valueOf(100_000_000)) > 0;

        // 3. Check if customer is VIP (purchaseCount > 10)
        boolean trustedCustomer = learning != null && learning.getPurchaseCount() > 10;

        // 4. Check high risk (rejectedCheckout > 3)
        boolean highRisk = learning != null && learning.getRejectedCheckout() > 3;

        // 5. Determine if VIP
        boolean isVIP = trustedCustomer && !highRisk;

        // 6. Check weekend
        boolean isWeekend = isWeekend(event.getTimestamp());

        // 7. Determine customer segment
        String customerSegment = determineCustomerSegment(learning, isVIP, highAmount);

        // 8. Determine payment category (simplified)
        String paymentCategory = determinePaymentCategory(event.getDecision());

        // 9. Determine recommended decision confidence
        String confidence = determineConfidence(isVIP, highAmount, trustedCustomer, highRisk);

        // 10. Use DRG fields from event if available
        String identityStatus = event.getIdentityStatus() != null ? event.getIdentityStatus() : "MATCH";
        String dukcapilStatus = event.getDukcapilStatus() != null ? event.getDukcapilStatus() : "VALID";
        String identityRisk = event.getIdentityRisk() != null ? event.getIdentityRisk() : "LOW";
        String transactionRisk = event.getTransactionRisk() != null ? event.getTransactionRisk() : "LOW";
        String overallRisk = event.getOverallRisk() != null ? event.getOverallRisk() : "LOW";
        Integer velocityRisk = event.getVelocityRisk() != null ? event.getVelocityRisk() : 0;
        Integer fraudScore = event.getFraudScore() != null ? event.getFraudScore() : 0;

        return new UnderstandingContext(
                isVIP,
                isFirstPurchase,
                isWeekend,
                highAmount,
                trustedCustomer,
                highRisk,
                customerSegment,
                paymentCategory,
                confidence,
                identityStatus,
                dukcapilStatus,
                identityRisk,
                transactionRisk,
                overallRisk,
                velocityRisk,
                fraudScore);
    }

    private boolean isWeekend(Instant timestamp) {
        if (timestamp == null)
            return false;
        LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        DayOfWeek day = dateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private String determineCustomerSegment(CustomerLearningEntity learning, boolean isVIP, boolean highAmount) {
        if (learning == null) {
            return "NEW_CUSTOMER";
        }
        if (isVIP) {
            return "VIP";
        }
        if (highAmount) {
            return "HIGH_VALUE";
        }
        if (learning.getPurchaseCount() > 5) {
            return "REGULAR";
        }
        return "STANDARD";
    }

    private String determinePaymentCategory(String decision) {
        if (decision == null)
            return "UNKNOWN";
        // Simple mapping based on decision string
        // In real implementation, this would come from event payload
        if (decision.contains("VA")) {
            return "VA";
        }
        if (decision.contains("CREDIT") || decision.contains("CC")) {
            return "CREDIT_CARD";
        }
        if (decision.contains("DEBIT") || decision.contains("DC")) {
            return "DEBIT";
        }
        return "DEFAULT";
    }

    private String determineConfidence(boolean isVIP, boolean highAmount, boolean trustedCustomer, boolean highRisk) {
        if (!isVIP || highRisk) {
            return "MEDIUM";
        }
        if (highAmount) {
            return "MEDIUM";
        }
        return "HIGH";
    }
}
