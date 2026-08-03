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
public class ExplanationResponse {
    private String checkoutId;
    private String decision;
    private String confidence;
    private String reason;
    private List<ExplanationItem> factors;
}