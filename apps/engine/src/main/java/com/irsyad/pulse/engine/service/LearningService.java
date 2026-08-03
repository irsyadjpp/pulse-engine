package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.response.LearningResponse;

public interface LearningService {
    LearningResponse getCustomerLearning(String customerId);
}