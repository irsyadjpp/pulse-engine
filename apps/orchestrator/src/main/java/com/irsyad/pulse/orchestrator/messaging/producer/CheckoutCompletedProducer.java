package com.irsyad.pulse.orchestrator.messaging.producer;

import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer for publishing CheckoutCompletedEvent to Kafka.
 */
@ApplicationScoped
public class CheckoutCompletedProducer {

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutCompletedProducer.class);

    @Channel("checkout-completed-out")
    Emitter<String> emitter;

    public void publish(String json) {
        LOG.info("Publishing checkout completed event: {}", json);
        emitter.send(json);
    }
}
