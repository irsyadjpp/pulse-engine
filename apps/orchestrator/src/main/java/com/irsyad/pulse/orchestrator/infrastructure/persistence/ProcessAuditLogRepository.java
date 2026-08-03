package com.irsyad.pulse.orchestrator.infrastructure.persistence;

import com.irsyad.pulse.orchestrator.domain.model.ProcessAuditLogEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProcessAuditLogRepository {

    public List<ProcessAuditLogEntity> findByProcessIdOrderByEventTimeAsc(String processId) {
        return ProcessAuditLogEntity.list("processId", processId);
    }

    public List<ProcessAuditLogEntity> findByCorrelationId(String correlationId) {
        return ProcessAuditLogEntity.list("correlationId", correlationId);
    }

    public List<ProcessAuditLogEntity> findByProcessIdAndEventType(String processId, String eventType) {
        // In Panache, we need to use a more complex query for multiple conditions
        return ProcessAuditLogEntity.<ProcessAuditLogEntity>list("processId", processId)
                .stream()
                .filter(log -> eventType.equals(log.getEventType()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public void save(ProcessAuditLogEntity entity) {
        entity.persist();
    }
}