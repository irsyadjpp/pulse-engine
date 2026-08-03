package com.irsyad.pulse.orchestrator.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessListResponse {
    private long total;
    private List<ProcessItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessItem {
        private String processId;
        private String status;
        private String currentNode;
    }
}