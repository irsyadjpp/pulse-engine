package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCaseResult {
    private String reviewId;
    private String status;
    private String assignedQueue;
    private Instant slaDueDate;
}
