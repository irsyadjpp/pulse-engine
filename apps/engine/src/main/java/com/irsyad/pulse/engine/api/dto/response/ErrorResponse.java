package com.irsyad.pulse.engine.api.dto.response;

import java.time.Instant;

public record ErrorResponse(
        String code,
        String message,
        String detail,
        Instant timestamp,
        String path
) {
}