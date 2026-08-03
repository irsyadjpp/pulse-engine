package com.irsyad.pulse.engine.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPattern {
    private String pattern;
    private String description;
    private int occurrences;
}