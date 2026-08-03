package com.irsyad.pulse.engine.model.dto;

import java.util.List;

public record InsightResponse(String orderId, List<InsightItem> insights) {
}