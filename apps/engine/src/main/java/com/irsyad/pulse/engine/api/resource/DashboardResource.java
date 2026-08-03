package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.service.DashboardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Dashboard", description = "Aggregated metrics and statistics for Pulse Engine")
public class DashboardResource {

    @Inject
    DashboardService dashboardService;

    @GET
    @Tag(name = "Dashboard")
    public Response getDashboard() {
        return Response.ok(dashboardService.getDashboard()).build();
    }
}
