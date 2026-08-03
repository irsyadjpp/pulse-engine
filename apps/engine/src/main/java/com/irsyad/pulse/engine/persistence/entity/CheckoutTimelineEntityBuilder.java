package com.irsyad.pulse.engine.persistence.entity;

import java.time.Instant;

/**
 * Builder for {@link CheckoutTimelineEntity}.
 */
public class CheckoutTimelineEntityBuilder {
    private final CheckoutTimelineEntity entity = new CheckoutTimelineEntity();

    public CheckoutTimelineEntityBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public CheckoutTimelineEntityBuilder checkoutId(String checkoutId) {
        entity.setCheckoutId(checkoutId);
        return this;
    }

    public CheckoutTimelineEntityBuilder capability(String capability) {
        entity.setCapability(capability);
        return this;
    }

    public CheckoutTimelineEntityBuilder status(String status) {
        entity.setStatus(status);
        return this;
    }

    public CheckoutTimelineEntityBuilder message(String message) {
        entity.setMessage(message);
        return this;
    }

    public CheckoutTimelineEntityBuilder processingTimeMs(Integer processingTimeMs) {
        entity.setProcessingTimeMs(processingTimeMs);
        return this;
    }

    public CheckoutTimelineEntityBuilder eventTime(Instant eventTime) {
        entity.setEventTime(eventTime);
        return this;
    }

    public CheckoutTimelineEntityBuilder createdAt(Instant createdAt) {
        entity.setCreatedAt(createdAt);
        return this;
    }

    public CheckoutTimelineEntity build() {
        return entity;
    }
}