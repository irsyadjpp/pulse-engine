package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.response.ExplanationResponse;

public interface ExplanationService {
    ExplanationResponse getExplanation(String checkoutId);
}