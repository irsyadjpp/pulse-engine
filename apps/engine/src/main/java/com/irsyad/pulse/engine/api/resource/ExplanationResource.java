package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.service.ExplanationService;
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
@Tag(name = "Explanations", description = "Human-readable explanations for checkout decisions")
public class ExplanationResource {

    @Inject
    ExplanationService explanationService;

    @GET
    @Path("/{checkoutId}/explanation")
    @Tag(name = "Explanations")
    public Response getExplanation(@PathParam("checkoutId") String checkoutId) {
        return Response.ok(explanationService.getExplanation(checkoutId)).build();
    }
}
