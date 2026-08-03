package com.irsyad.pulse.engine.model.dto;

import java.time.Instant;

public record ErrorResponse(
        String code,
        String message,
        String detail,
        Instant timestamp,
        String path
) {
}