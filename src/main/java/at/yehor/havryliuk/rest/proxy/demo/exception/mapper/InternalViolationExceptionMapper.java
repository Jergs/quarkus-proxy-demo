package at.yehor.havryliuk.rest.proxy.demo.exception.mapper;

import at.yehor.havryliuk.rest.proxy.demo.exception.InternalViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InternalViolationExceptionMapper implements ExceptionMapper<InternalViolationException> {

    public Response toResponse(InternalViolationException e) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }

}
