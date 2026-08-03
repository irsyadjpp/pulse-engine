package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.api.dto.request.SearchInsightRequest;
import com.irsyad.pulse.engine.api.dto.response.InsightItemResponse;

import java.util.List;

public interface InsightService {
    InsightItemResponse getInsight(String checkoutId);
    List<InsightItemResponse> searchInsights(SearchInsightRequest request);
}