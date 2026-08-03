package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.response.DashboardResponse;
import com.irsyad.pulse.engine.persistence.mapper.DashboardMapper;
import com.irsyad.pulse.engine.persistence.repository.CheckoutInsightRepository;
import com.irsyad.pulse.engine.persistence.repository.CheckoutTimelineRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DashboardServiceImpl implements DashboardService {

    @Inject
    CheckoutInsightRepository checkoutInsightRepository;

    @Inject
    CheckoutTimelineRepository checkoutTimelineRepository;

    @Override
    public DashboardResponse getDashboard() {
        long total = checkoutInsightRepository.countAll();
        long approved = checkoutInsightRepository.countByDecision("APPROVE");
        long rejected = checkoutInsightRepository.countByDecision("REJECT");
        long review = total - approved - rejected;

        double avgTime = checkoutTimelineRepository.getAverageProcessingTimeMs();

        return DashboardMapper.toResponse(total, approved, review, rejected, avgTime);
    }
}
