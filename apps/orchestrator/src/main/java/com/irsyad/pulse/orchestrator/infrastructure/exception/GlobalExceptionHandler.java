package com.irsyad.pulse.orchestrator.infrastructure.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Map<String, Object> error = new HashMap<>();
        
        if (exception instanceof IllegalArgumentException) {
            error.put("error", "Bad Request");
            error.put("message", exception.getMessage());
            error.put("type", "IllegalArgumentException");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        error.put("error", "Internal Server Error");
        error.put("message", exception.getMessage());
        error.put("type", exception.getClass().getSimpleName());
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}