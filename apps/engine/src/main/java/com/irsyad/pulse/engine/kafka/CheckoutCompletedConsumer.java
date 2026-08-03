package com.irsyad.pulse.engine.kafka;

import com.irsyad.pulse.engine.event.CheckoutCompletedEvent;
import com.irsyad.pulse.engine.pipeline.PulseEnginePipeline;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CheckoutCompletedConsumer {

    private static final Logger LOG = Logger.getLogger(CheckoutCompletedConsumer.class);

    @Inject
    PulseEnginePipeline pipeline;

    @Incoming("checkout.completed")
    @Blocking
    public void process(CheckoutCompletedEvent event) {
        LOG.info("Received checkout.completed event: " + event.getBusinessKey());
        pipeline.execute(event);
    }
}