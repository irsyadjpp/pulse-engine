package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.service.LearningService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Learning", description = "Customer learning profiles and behavioral patterns")
public class LearningResource {

    @Inject
    LearningService learningService;

    @GET
    @Path("/{customerId}/learning")
    @Tag(name = "Learning")
    public Response getCustomerLearning(@PathParam("customerId") String customerId) {
        return Response.ok(learningService.getCustomerLearning(customerId)).build();
    }
}
