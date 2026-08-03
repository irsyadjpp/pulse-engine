package com.irsyad.pulse.engine.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SearchInsightRequest(
        String severity,
        String type,
        String customerId,
        String checkoutId,

        @Min(value = 0, message = "page must be >= 0")
        int page,

        @Min(value = 1, message = "size must be >= 1")
        @Max(value = 100, message = "size must be <= 100")
        int size
) {
    public SearchInsightRequest {
        if (page < 0) page = 0;
        if (size <= 0) size = 20;
        if (size > 100) size = 100;
    }
}