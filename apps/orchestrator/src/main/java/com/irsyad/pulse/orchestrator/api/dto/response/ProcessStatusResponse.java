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
public class ProcessStatusResponse {

    private String processId;
    private String processName;
    private String status;
    private String nodeNames;
    private Instant startTime;
}