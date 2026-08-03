package com.irsyad.pulse.orchestrator.infrastructure.persistence;

import com.irsyad.pulse.orchestrator.domain.model.ProcessMessageEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class ProcessMessageRepository {

    public List<ProcessMessageEntity> findByProcessIdOrderByCreatedAtAsc(String processId) {
        return ProcessMessageEntity.list("processId", processId);
    }

    public List<ProcessMessageEntity> findByStatus(String status) {
        return ProcessMessageEntity.list("status", status);
    }

    public List<ProcessMessageEntity> findByTopic(String topic) {
        return ProcessMessageEntity.list("topic", topic);
    }

    public long countByStatus(String status) {
        return ProcessMessageEntity.count("status", status);
    }

    public ProcessMessageEntity findByEventId(String eventId) {
        return ProcessMessageEntity.find("eventId", eventId).firstResult();
    }

    public ProcessMessageEntity findTopByProcessIdOrderByCreatedAtDesc(String processId) {
        return ProcessMessageEntity.find("processId", processId).firstResult();
    }

    public int deleteByCreatedAtBefore(Instant cutoffTime) {
        return (int) ProcessMessageEntity.delete("createdAt < ?1", cutoffTime);
    }
    
    public void save(ProcessMessageEntity entity) {
        entity.persist();
    }
}