package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.response.EventTimelineResponse;

public interface TimelineService {
    EventTimelineResponse getTimeline(String checkoutId);
}