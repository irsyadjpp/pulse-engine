package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.api.dto.response.LearningResponse;
import com.irsyad.pulse.engine.model.dto.ErrorResponse;
import com.irsyad.pulse.engine.service.LearningService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.time.Instant;

@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Learning", description = "Customer learning profiles and behavioral patterns")
public class LearningResource {

    @Inject
    LearningService learningService;

    private static final Logger LOG = Logger.getLogger(LearningResource.class);

    @GET
    @Path("/{customerId}/learning")
    @Tag(name = "Learning")
    public Response getCustomerLearning(@PathParam("customerId") String customerId) {
        LearningResponse learning = learningService.getCustomerLearning(customerId);
        if (learning == null) {
            LOG.warn("Learning profile not found for customerId: " + customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(
                            "NOT_FOUND",
                            "Learning profile not found for customerId: " + customerId,
                            null,
                            Instant.now(),
                            "/api/v1/customers/" + customerId + "/learning"))
                    .build();
        }
        return Response.ok(learning).build();
    }
}
