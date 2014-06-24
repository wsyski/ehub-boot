package com.axiell.ehub.util;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException exception) {
        LOGGER.error(exception.getMessage(), exception);
        final EhubError ehubError = ErrorCause.INTERNAL_SERVER_ERROR.toEhubError();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(ehubError).build();
    }
}
