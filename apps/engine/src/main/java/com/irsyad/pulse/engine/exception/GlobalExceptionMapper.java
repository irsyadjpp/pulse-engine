package com.irsyad.pulse.engine.exception;

import com.irsyad.pulse.engine.api.dto.response.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.Instant;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        // Let WebApplicationException (e.g. 404, 400) pass through with its own status
        if (exception instanceof WebApplicationException wae) {
            return wae.getResponse();
        }

        LOG.error("Unhandled exception: " + exception.getMessage(), exception);

        ErrorResponse error = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                Instant.now(),
                null
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}
