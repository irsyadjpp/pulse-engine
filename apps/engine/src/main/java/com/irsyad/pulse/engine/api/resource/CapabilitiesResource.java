package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.service.CapabilitiesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/capabilities")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Capabilities", description = "Pulse Engine capability status (Observe, Understand, Explain, Learn, Persist, Publish)")
public class CapabilitiesResource {

    @Inject
    CapabilitiesService capabilitiesService;

    @GET
    @Tag(name = "Capabilities")
    public Response getCapabilities() {
        return Response.ok(capabilitiesService.getCapabilities()).build();
    }
}
