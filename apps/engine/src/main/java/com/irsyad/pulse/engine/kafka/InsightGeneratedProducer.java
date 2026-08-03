package com.irsyad.pulse.engine.kafka;

import com.irsyad.pulse.engine.event.InsightGeneratedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InsightGeneratedProducer {

    private static final Logger LOG = Logger.getLogger(InsightGeneratedProducer.class);

    @Inject
    @Channel("insight.generated")
    Emitter<InsightGeneratedEvent> emitter;

    public void publish(InsightGeneratedEvent event) {
        LOG.info("Publishing insight.generated for: " + event.getOrderId());
        emitter.send(event);
    }
}