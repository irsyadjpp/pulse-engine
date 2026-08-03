package com.irsyad.pulse.orchestrator.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutProcessResponse {
    private String processId;
    private String businessKey;
    private String status;
    private String currentNode;
    private String decision;
    private Instant startedAt;
    private Instant lastUpdated;
}