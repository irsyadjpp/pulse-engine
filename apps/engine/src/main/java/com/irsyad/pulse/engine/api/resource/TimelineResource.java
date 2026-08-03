package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.service.TimelineService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/insights")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Timeline", description = "Event timeline for a checkout insight")
public class TimelineResource {

    @Inject
    TimelineService timelineService;

    @GET
    @Path("/{checkoutId}/timeline")
    @Tag(name = "Timeline")
    public Response getTimeline(@PathParam("checkoutId") String checkoutId) {
        return Response.ok(timelineService.getTimeline(checkoutId)).build();
    }
}
