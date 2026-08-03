package com.irsyad.pulse.orchestrator.application;

import com.irsyad.pulse.orchestrator.domain.model.ProcessMessageEntity;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessMessageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
public class IdempotencyService {

    private static final Logger LOG = LoggerFactory.getLogger(IdempotencyService.class);
    private static final Duration IDEMPOTENCY_WINDOW = Duration.ofHours(24);

    @Inject
    private ProcessMessageRepository processMessageRepository;

    public boolean isDuplicateEvent(String eventId) {
        ProcessMessageEntity existingMessage = 
                processMessageRepository.findByEventId(eventId);
        
        if (existingMessage != null) {
            // Check if the event is within the idempotency window
            if (existingMessage.getCreatedAt().plus(IDEMPOTENCY_WINDOW).isAfter(Instant.now())) {
                LOG.warn("Duplicate event detected: {} (original: {})", eventId, existingMessage.getCreatedAt());
                return true;
            }
        }
        
        return false;
    }

    public void recordEvent(String eventId, String processId, String topic, String eventName, String payload) {
        ProcessMessageEntity message = new ProcessMessageEntity(
                processId, topic, eventName, payload, "SUCCESS"
        );
        message.setEventId(eventId);
        message.setPublishedAt(Instant.now());
        processMessageRepository.save(message);
        
        LOG.info("Recorded event for idempotency: {} process: {}", eventId, processId);
    }

    public ProcessMessageEntity getExistingEvent(String eventId) {
        return processMessageRepository.findByEventId(eventId);
    }

    public void cleanupExpiredEvents() {
        Instant cutoffTime = Instant.now().minus(IDEMPOTENCY_WINDOW);
        int deletedCount = processMessageRepository.deleteByCreatedAtBefore(cutoffTime);
        LOG.info("Cleaned up {} expired idempotency records older than {}", deletedCount, cutoffTime);
    }
}