package com.irsyad.pulse.orchestrator.api;

import com.irsyad.pulse.orchestrator.api.dto.request.CancelRequest;
import com.irsyad.pulse.orchestrator.api.dto.request.CheckoutApiRequest;
import com.irsyad.pulse.orchestrator.api.dto.request.RetryRequest;
import com.irsyad.pulse.orchestrator.api.dto.response.*;
import com.irsyad.pulse.orchestrator.application.CheckoutApplicationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckoutResource {

    @Inject
    private CheckoutApplicationService checkoutService;

    // 1. Start Checkout Process
    @POST
    @Path("/checkouts")
    public Response startCheckout(CheckoutApiRequest request) {
        CheckoutStartResponse response = checkoutService.startCheckout(request);
        return Response.accepted().entity(response).build();
    }

    // 2. Get Checkout Process
    @GET
    @Path("/checkouts/{processId}")
    public Response getCheckout(@PathParam("processId") String processId) {
        CheckoutProcessResponse response = checkoutService.getCheckoutProcess(processId);
        return Response.ok(response).build();
    }

    // 3. Get Process Timeline
    @GET
    @Path("/checkouts/{processId}/timeline")
    public Response getTimeline(@PathParam("processId") String processId) {
        ProcessTimelineResponse response = checkoutService.getProcessTimeline(processId);
        return Response.ok(response).build();
    }

    // 4. Get Decision
    @GET
    @Path("/checkouts/{processId}/decision")
    public Response getDecision(@PathParam("processId") String processId) {
        DecisionResponse response = checkoutService.getDecision(processId);
        return Response.ok(response).build();
    }

    // 5. Retry Process
    @POST
    @Path("/checkouts/{processId}/retry")
    public Response retryProcess(@PathParam("processId") String processId, RetryRequest request) {
        RetryResponse response = checkoutService.retryProcess(processId, request);
        return Response.ok(response).build();
    }

    // 6. Cancel Process
    @POST
    @Path("/checkouts/{processId}/cancel")
    public Response cancelProcess(@PathParam("processId") String processId, CancelRequest request) {
        CancelResponse response = checkoutService.cancelProcess(processId, request);
        return Response.ok(response).build();
    }

    // 7. List Checkouts
    @GET
    @Path("/checkouts")
    public Response listCheckouts(
            @QueryParam("status") String status,
            @QueryParam("customerId") String customerId,
            @QueryParam("decision") String decision) {
        ProcessListResponse response = checkoutService.listProcesses(status, customerId, decision);
        return Response.ok(response).build();
    }

    // 8. Process Statistics (Dashboard)
    @GET
    @Path("/dashboard")
    public Response getDashboard() {
        DashboardResponse response = checkoutService.getDashboard();
        return Response.ok(response).build();
    }
}
