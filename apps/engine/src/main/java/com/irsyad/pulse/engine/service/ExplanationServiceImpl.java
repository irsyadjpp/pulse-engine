package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.response.ExplanationResponse;
import com.irsyad.pulse.engine.persistence.entity.CheckoutExplanationEntity;
import com.irsyad.pulse.engine.persistence.mapper.ExplanationMapper;
import com.irsyad.pulse.engine.persistence.repository.CheckoutExplanationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ExplanationServiceImpl implements ExplanationService {

    @Inject
    CheckoutExplanationRepository checkoutExplanationRepository;

    @Override
    public ExplanationResponse getExplanation(String checkoutId) {
        List<CheckoutExplanationEntity> entities = checkoutExplanationRepository.findByCheckoutId(checkoutId);
        return ExplanationMapper.toResponse(checkoutId, entities);
    }
}
