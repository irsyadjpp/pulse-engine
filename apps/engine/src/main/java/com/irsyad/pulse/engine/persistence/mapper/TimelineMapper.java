package com.irsyad.pulse.engine.persistence.mapper;

import com.irsyad.pulse.engine.api.dto.response.EventEntry;
import com.irsyad.pulse.engine.api.dto.response.EventTimelineResponse;
import com.irsyad.pulse.engine.persistence.entity.CheckoutTimelineEntity;

import java.util.List;

public class TimelineMapper {

    public static EventTimelineResponse toResponse(String checkoutId, List<CheckoutTimelineEntity> entities) {
        EventTimelineResponse response = EventTimelineResponse.builder()
                .checkoutId(checkoutId)
                .build();

        if (entities != null) {
            response.setEvents(entities.stream()
                    .map(entity -> EventEntry.builder()
                            .eventName(entity.getCapability())
                            .time(entity.getEventTime())
                            .build())
                    .toList());
        }

        return response;
    }
}