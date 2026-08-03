package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeCheckoutResult {
    private String checkoutId;
    private String status;
    private Instant completedAt;
}
