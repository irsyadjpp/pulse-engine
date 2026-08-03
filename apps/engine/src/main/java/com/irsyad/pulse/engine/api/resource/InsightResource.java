package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.api.dto.request.SearchInsightRequest;
import com.irsyad.pulse.engine.api.dto.response.InsightItemResponse;
import com.irsyad.pulse.engine.api.dto.response.PagedResponse;
import com.irsyad.pulse.engine.api.dto.response.ErrorResponse;
import com.irsyad.pulse.engine.service.InsightService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.List;

@Path("/api/v1/insights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Insights", description = "Checkout insights and decision results from Pulse Engine")
public class InsightResource {

    @Inject
    InsightService insightService;

    private static final Logger LOG = Logger.getLogger(InsightResource.class);

    @GET
    @Path("/{checkoutId}")
    @Tag(name = "Insights")
    public Response getInsight(@PathParam("checkoutId") String checkoutId) {
        InsightItemResponse insight = insightService.getInsight(checkoutId);
        if (insight == null) {
            LOG.warn("Insight not found for checkoutId: " + checkoutId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(
                            "NOT_FOUND",
                            "Insight not found for checkoutId: " + checkoutId,
                            null,
                            Instant.now(),
                            "/api/v1/insights/" + checkoutId))
                    .build();
        }
        return Response.ok(insight).build();
    }

    @POST
    @Path("/search")
    @Tag(name = "Insights")
    public Response searchInsights(@Valid SearchInsightRequest request) {
        List<InsightItemResponse> results = insightService.searchInsights(request);
        PagedResponse<InsightItemResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(results);
        pagedResponse.setTotalElements(results.size());
        pagedResponse.setPage(request.page());
        pagedResponse.setSize(request.size());
        return Response.ok(pagedResponse).build();
    }
}
