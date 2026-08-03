package com.irsyad.pulse.engine.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Builder for {@link CustomerLearningEntity}.
 */
public class CustomerLearningEntityBuilder {
    private final CustomerLearningEntity entity = new CustomerLearningEntity();

    public CustomerLearningEntityBuilder customerId(String customerId) {
        entity.setCustomerId(customerId);
        return this;
    }

    public CustomerLearningEntityBuilder purchaseCount(Integer purchaseCount) {
        entity.setPurchaseCount(purchaseCount);
        return this;
    }

    public CustomerLearningEntityBuilder successfulCheckout(Integer successfulCheckout) {
        entity.setSuccessfulCheckout(successfulCheckout);
        return this;
    }

    public CustomerLearningEntityBuilder rejectedCheckout(Integer rejectedCheckout) {
        entity.setRejectedCheckout(rejectedCheckout);
        return this;
    }

    public CustomerLearningEntityBuilder averageAmount(BigDecimal averageAmount) {
        entity.setAverageAmount(averageAmount);
        return this;
    }

    public CustomerLearningEntityBuilder highestAmount(BigDecimal highestAmount) {
        entity.setHighestAmount(highestAmount);
        return this;
    }

    public CustomerLearningEntityBuilder preferredPaymentMethod(String preferredPaymentMethod) {
        entity.setPreferredPaymentMethod(preferredPaymentMethod);
        return this;
    }

    public CustomerLearningEntityBuilder customerSegment(String customerSegment) {
        entity.setCustomerSegment(customerSegment);
        return this;
    }

    public CustomerLearningEntityBuilder lastCheckoutTime(Instant lastCheckoutTime) {
        entity.setLastCheckoutTime(lastCheckoutTime);
        return this;
    }

    public CustomerLearningEntityBuilder learningVersion(Integer learningVersion) {
        entity.setLearningVersion(learningVersion);
        return this;
    }

    public CustomerLearningEntityBuilder createdAt(Instant createdAt) {
        entity.setCreatedAt(createdAt);
        return this;
    }

    public CustomerLearningEntityBuilder updatedAt(Instant updatedAt) {
        entity.setUpdatedAt(updatedAt);
        return this;
    }

    public CustomerLearningEntity build() {
        return entity;
    }
}