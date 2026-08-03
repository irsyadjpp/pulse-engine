package com.irsyad.pulse.engine.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.Map;

@ApplicationScoped
public class CapabilitiesService {

    private static final Logger LOG = Logger.getLogger(CapabilitiesService.class);

    public Map<String, String> getCapabilities() {
        LOG.info("Fetching capabilities status");
        return Map.of(
                "observe", "UP",
                "understand", "UP",
                "explain", "UP",
                "learn", "UP",
                "persist", "UP",
                "publish", "UP"
        );
    }
}