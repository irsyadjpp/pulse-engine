package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.response.EventTimelineResponse;
import com.irsyad.pulse.engine.persistence.entity.CheckoutTimelineEntity;
import com.irsyad.pulse.engine.persistence.mapper.TimelineMapper;
import com.irsyad.pulse.engine.persistence.repository.CheckoutTimelineRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TimelineServiceImpl implements TimelineService {

    @Inject
    CheckoutTimelineRepository checkoutTimelineRepository;

    @Override
    public EventTimelineResponse getTimeline(String checkoutId) {
        List<CheckoutTimelineEntity> entities = checkoutTimelineRepository.findByCheckoutIdOrderByEventTimeAsc(checkoutId);
        return TimelineMapper.toResponse(checkoutId, entities);
    }
}
