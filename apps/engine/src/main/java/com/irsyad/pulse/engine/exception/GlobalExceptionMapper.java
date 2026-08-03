package com.irsyad.pulse.engine.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOG.error("Unhandled exception: " + exception.getMessage(), exception);

        Map<String, Object> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", exception.getMessage());
        error.put("type", exception.getClass().getSimpleName());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity(error)
                .build();
    }
}