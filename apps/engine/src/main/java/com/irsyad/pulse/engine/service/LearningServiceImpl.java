package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.response.LearningResponse;
import com.irsyad.pulse.engine.persistence.entity.CustomerLearningEntity;
import com.irsyad.pulse.engine.persistence.mapper.LearningMapper;
import com.irsyad.pulse.engine.persistence.repository.CustomerLearningRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LearningServiceImpl implements LearningService {

    @Inject
    CustomerLearningRepository customerLearningRepository;

    @Override
    public LearningResponse getCustomerLearning(String customerId) {
        CustomerLearningEntity entity = customerLearningRepository.findById(customerId);
        if (entity == null) {
            return null;
        }
        return LearningMapper.toResponse(entity);
    }
}
