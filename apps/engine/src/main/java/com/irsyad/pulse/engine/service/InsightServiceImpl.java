package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.request.SearchInsightRequest;
import com.irsyad.pulse.engine.api.dto.response.InsightItemResponse;
import com.irsyad.pulse.engine.persistence.entity.CheckoutInsightEntity;
import com.irsyad.pulse.engine.persistence.mapper.InsightMapper;
import com.irsyad.pulse.engine.persistence.repository.CheckoutInsightRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class InsightServiceImpl implements InsightService {

    @Inject
    CheckoutInsightRepository checkoutInsightRepository;

    @Override
    public InsightItemResponse getInsight(String checkoutId) {
        CheckoutInsightEntity entity = checkoutInsightRepository.findById(checkoutId);
        if (entity == null) {
            return null;
        }
        return InsightMapper.toResponse(entity);
    }

    @Override
    public List<InsightItemResponse> searchInsights(SearchInsightRequest request) {
        // Idiomatic Panache query with pagination; repository handles the dynamic
        // WHERE clause and ORDER BY processedAt DESC.
        List<CheckoutInsightEntity> entities = checkoutInsightRepository.search(
                request.checkoutId(),
                request.customerId(),
                request.severity(),
                request.type(),
                request.page(),
                request.size());

        return entities.stream()
                .map(InsightMapper::toResponse)
                .toList();
    }
}
