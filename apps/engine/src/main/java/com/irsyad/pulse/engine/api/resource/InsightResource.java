package com.irsyad.pulse.engine.api.resource;

import com.irsyad.pulse.engine.api.dto.request.SearchInsightRequest;
import com.irsyad.pulse.engine.api.dto.response.InsightItemResponse;
import com.irsyad.pulse.engine.api.dto.response.PagedResponse;
import com.irsyad.pulse.engine.service.InsightService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/v1/insights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Insights", description = "Checkout insights and decision results from Pulse Engine")
public class InsightResource {

    @Inject
    InsightService insightService;

    @GET
    @Path("/{checkoutId}")
    @Tag(name = "Insights")
    public Response getInsight(@PathParam("checkoutId") String checkoutId) {
        return Response.ok(insightService.getInsight(checkoutId)).build();
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
