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
        // Build dynamic query based on available filters in SearchInsightRequest
        StringBuilder query = new StringBuilder("SELECT i FROM CheckoutInsightEntity i WHERE 1=1");

        if (request.checkoutId() != null && !request.checkoutId().isEmpty()) {
            query.append(" AND i.checkoutId LIKE :checkoutId");
        }
        if (request.customerId() != null && !request.customerId().isEmpty()) {
            query.append(" AND i.customerId LIKE :customerId");
        }
        if (request.severity() != null && !request.severity().isEmpty()) {
            query.append(" AND i.confidence = :severity");
        }
        if (request.type() != null && !request.type().isEmpty()) {
            query.append(" AND i.decision = :type");
        }

        // Default sort by processedAt DESC
        query.append(" ORDER BY i.processedAt DESC");

        // Create query
        jakarta.persistence.Query q = checkoutInsightRepository.getEntityManager()
                .createQuery(query.toString(), CheckoutInsightEntity.class);

        // Set parameters
        if (request.checkoutId() != null && !request.checkoutId().isEmpty()) {
            q.setParameter("checkoutId", "%" + request.checkoutId() + "%");
        }
        if (request.customerId() != null && !request.customerId().isEmpty()) {
            q.setParameter("customerId", "%" + request.customerId() + "%");
        }
        if (request.severity() != null && !request.severity().isEmpty()) {
            q.setParameter("severity", request.severity());
        }
        if (request.type() != null && !request.type().isEmpty()) {
            q.setParameter("type", request.type());
        }

        // Pagination
        int page = request.page();
        int size = request.size();
        q.setFirstResult(page * size);
        q.setMaxResults(size);

        List<CheckoutInsightEntity> entities = q.getResultList();
        return entities.stream()
                .map(InsightMapper::toResponse)
                .toList();
    }
}
