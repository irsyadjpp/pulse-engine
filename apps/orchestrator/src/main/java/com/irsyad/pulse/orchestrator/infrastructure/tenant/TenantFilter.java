package com.irsyad.pulse.orchestrator.infrastructure.tenant;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Provider
public class TenantFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(TenantFilter.class);
    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_TENANT = "default";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String tenantId = requestContext.getHeaderString(TENANT_HEADER);
        
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = DEFAULT_TENANT;
            LOG.debug("No tenant header found, using default tenant: {}", DEFAULT_TENANT);
        } else {
            LOG.debug("Tenant header found: {}", tenantId);
        }
        
        TenantContext.setTenant(tenantId);
    }
}