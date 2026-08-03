package com.irsyad.pulse.product.infrastructure.flyway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway database migration configuration (Appendix N, TSD_03).
 *
 * <p>In Spring Boot 4 the Flyway integration lives in the
 * {@code org.springframework.boot.flyway.autoconfigure} module. The
 * auto-configuration already runs migrations on startup; this configuration
 * simply documents where migration scripts are expected
 * (src/main/resources/db/migration).
 */
@Configuration
public class FlywayConfig {

    @Bean
    public String flywayMigrationLocation() {
        return "classpath:db/migration";
    }
}
