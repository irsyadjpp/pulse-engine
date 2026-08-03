package com.irsyad.pulse.orchestrator.infrastructure.tenant;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class TenantConfig {

    @Produces
    @Singleton
    public TenantRegistry tenantRegistry() {
        Map<String, TenantConfiguration> tenants = new HashMap<>();
        
        // Default tenant configuration
        tenants.put("default", new TenantConfiguration(
            "default",
            "pulse.default",
            "pulse.checkout.completed.v1",
            1000
        ));
        
        // Example tenant configurations
        tenants.put("tenant1", new TenantConfiguration(
            "tenant1",
            "pulse.tenant1",
            "pulse.checkout.completed.v1.tenant1",
            5000
        ));
        
        tenants.put("tenant2", new TenantConfiguration(
            "tenant2",
            "pulse.tenant2",
            "pulse.checkout.completed.v1.tenant2",
            10000
        ));
        
        return new TenantRegistry(tenants);
    }

    public static class TenantConfiguration {
        private final String tenantId;
        private final String schemaPrefix;
        private final String kafkaTopicPrefix;
        private final int resourceQuota;

        public TenantConfiguration(String tenantId, String schemaPrefix, String kafkaTopicPrefix, int resourceQuota) {
            this.tenantId = tenantId;
            this.schemaPrefix = schemaPrefix;
            this.kafkaTopicPrefix = kafkaTopicPrefix;
            this.resourceQuota = resourceQuota;
        }

        // Getters
        public String getTenantId() { return tenantId; }
        public String getSchemaPrefix() { return schemaPrefix; }
        public String getKafkaTopicPrefix() { return kafkaTopicPrefix; }
        public int getResourceQuota() { return resourceQuota; }
    }

    public static class TenantRegistry {
        private final Map<String, TenantConfiguration> tenants;

        public TenantRegistry(Map<String, TenantConfiguration> tenants) {
            this.tenants = tenants;
        }

        public TenantConfiguration getTenant(String tenantId) {
            return tenants.get(tenantId);
        }

        public boolean tenantExists(String tenantId) {
            return tenants.containsKey(tenantId);
        }

        public String getTenantTopic(String tenantId, String baseTopic) {
            TenantConfiguration config = tenants.get(tenantId);
            if (config != null) {
                return config.getKafkaTopicPrefix() + "." + baseTopic;
            }
            return baseTopic;
        }
    }
}