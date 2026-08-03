package com.irsyad.pulse.engine.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private long totalProcessed;
    private long approved;
    private long review;
    private long rejected;
    private double avgProcessingTimeMs;
    private List<DashboardMetric> metrics;
}