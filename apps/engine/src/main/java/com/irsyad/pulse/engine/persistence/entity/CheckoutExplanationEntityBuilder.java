package com.irsyad.pulse.engine.persistence.entity;

import java.time.Instant;

/**
 * Builder for {@link CheckoutExplanationEntity}.
 */
public class CheckoutExplanationEntityBuilder {
    private final CheckoutExplanationEntity entity = new CheckoutExplanationEntity();

    public CheckoutExplanationEntityBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public CheckoutExplanationEntityBuilder checkoutId(String checkoutId) {
        entity.setCheckoutId(checkoutId);
        return this;
    }

    public CheckoutExplanationEntityBuilder explanationType(String explanationType) {
        entity.setExplanationType(explanationType);
        return this;
    }

    public CheckoutExplanationEntityBuilder explanation(String explanation) {
        entity.setExplanation(explanation);
        return this;
    }

    public CheckoutExplanationEntityBuilder createdAt(Instant createdAt) {
        entity.setCreatedAt(createdAt);
        return this;
    }

    public CheckoutExplanationEntity build() {
        return entity;
    }
}