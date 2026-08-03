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
public class LearningResponse {
    private String customerId;
    private String segment;
    private List<LearningPattern> patterns;
    private List<LearningMetric> metrics;
}