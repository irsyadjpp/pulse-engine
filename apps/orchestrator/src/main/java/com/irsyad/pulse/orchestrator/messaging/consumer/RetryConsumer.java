package com.irsyad.pulse.orchestrator.messaging.consumer;

import com.irsyad.pulse.orchestrator.domain.model.ProcessMessageEntity;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessMessageRepository;
import com.irsyad.pulse.orchestrator.messaging.event.CheckoutCompletedEvent;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class RetryConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(RetryConsumer.class);
    private static final int MAX_RETRY_COUNT = 3;

    @Inject
    private ProcessMessageRepository processMessageRepository;

    @Incoming("checkout-retry")
    @Blocking
    public CompletionStage<Void> handleRetry(Message<CheckoutCompletedEvent> message) {
        CheckoutCompletedEvent event = message.getPayload();
        String processId = event.getProcessId();
        LOG.info("Received retry event for process: {}", processId);

        try {
            List<ProcessMessageEntity> messages = processMessageRepository
                    .findByProcessIdOrderByCreatedAtAsc(processId);

            ProcessMessageEntity originalMessage = findLatestMessage(messages);

            if (originalMessage != null && "FAILED".equals(originalMessage.getStatus())) {
                LOG.info("Retrying failed message for process: {} retry count: {}",
                        processId, originalMessage.getRetryCount());

                originalMessage.incrementRetryCount();
                originalMessage.setStatus("RETRYING");
                processMessageRepository.save(originalMessage);

                if (originalMessage.getRetryCount() < MAX_RETRY_COUNT) {
                    LOG.info("Retrying process: {} with exponential backoff", processId);
                    originalMessage.setStatus("PENDING");
                    processMessageRepository.save(originalMessage);
                } else {
                    LOG.warn("Max retries exceeded for process: {}, sending to DLQ", processId);
                    sendToDLQ(originalMessage);
                }
            }

            return message.ack();

        } catch (Exception e) {
            LOG.error("Error processing retry event for process: {}", processId, e);
            return message.nack(e);
        }
    }

    private ProcessMessageEntity findLatestMessage(List<ProcessMessageEntity> messages) {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }

    private void sendToDLQ(ProcessMessageEntity message) {
        try {
            ProcessMessageEntity dlqMessage = new ProcessMessageEntity(
                    message.getProcessId(),
                    message.getTopic() + ".dlq",
                    message.getEventName(),
                    message.getPayload(),
                    "DLQ"
            );
            dlqMessage.setRetryCount(message.getRetryCount());
            processMessageRepository.save(dlqMessage);

            LOG.info("Message sent to DLQ for process: {}", message.getProcessId());
        } catch (Exception e) {
            LOG.error("Failed to send message to DLQ for process: {}", message.getProcessId(), e);
        }
    }
}