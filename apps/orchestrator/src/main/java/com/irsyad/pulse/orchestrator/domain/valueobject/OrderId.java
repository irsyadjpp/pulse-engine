package com.irsyad.pulse.orchestrator.domain.valueobject;

import java.util.Objects;

public record OrderId(String value) {

    public OrderId {
        Objects.requireNonNull(value, "orderId must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
    }

    public static OrderId of(String value) {
        return new OrderId(value);
    }
}