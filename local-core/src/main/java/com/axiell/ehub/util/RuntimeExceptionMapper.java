package com.axiell.ehub.util;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper extends AbstractEhubExceptionMapper<RuntimeException> implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(final RuntimeException exception) {
        LOGGER.error(exception.getMessage(), exception);
        final EhubError ehubError = ErrorCause.INTERNAL_SERVER_ERROR.toEhubError();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(getMediaType()).entity(ehubError).build();
    }
}
