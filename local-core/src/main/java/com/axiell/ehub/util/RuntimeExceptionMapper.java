package com.axiell.ehub.util;

import com.axiell.authinfo.InvalidAuthorizationHeaderSignatureRuntimeException;
import com.axiell.authinfo.MissingOrUnparseableAuthorizationHeaderRuntimeException;
import com.axiell.authinfo.MissingSecretKeyRuntimeException;
import com.axiell.ehub.*;
import com.axiell.ehub.security.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper extends AbstractEhubExceptionMapper<RuntimeException> implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(final RuntimeException exception) {
        final Throwable cause = exception.getCause() == null ? exception : exception.getCause();

        if (cause instanceof UnauthorizedException) {
            final UnauthorizedException ehubException = (UnauthorizedException) cause;
            return handleEhubRuntimeException(ehubException);
        } else if (cause instanceof ForbiddenException) {
            final ForbiddenException ehubException = (ForbiddenException) cause;
            return handleEhubRuntimeException(ehubException);
        } else if (cause instanceof InternalServerErrorException) {
            final InternalServerErrorException ehubException = (InternalServerErrorException) cause;
            return handleEhubRuntimeException(ehubException);
        } else if (cause instanceof MissingOrUnparseableAuthorizationHeaderRuntimeException) {
            EhubRuntimeException ehubException = new UnauthorizedException(getCauseMessage(cause), ErrorCause.MISSING_AUTHORIZATION_HEADER);
            return handleEhubRuntimeException(ehubException);
        } else if (cause instanceof MissingSecretKeyRuntimeException) {
            EhubRuntimeException ehubException = new UnauthorizedException( getCauseMessage(cause), ErrorCause.MISSING_SECRET_KEY);
            return handleEhubRuntimeException(ehubException);
        } else if (cause instanceof InvalidAuthorizationHeaderSignatureRuntimeException) {
            EhubRuntimeException ehubException = new UnauthorizedException( getCauseMessage(cause), ErrorCause.INVALID_SIGNATURE);
            return handleEhubRuntimeException(ehubException);
        } else {
            logException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), getCauseMessage(cause));
            final EhubError ehubError = ErrorCause.INTERNAL_SERVER_ERROR.toEhubError();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(getMediaType()).entity(ehubError).build();
        }
    }

    private Response handleEhubRuntimeException(final EhubRuntimeException ehubException) {
        logException(ehubException.getStatus(),ehubException.getMessage());
        return ehubException.getResponse(getMediaType());
    }

    private void logException(final int status, final String message) {
        LOGGER.error("Status: "+ status +" "+message);
    }

    private static String getCauseMessage(final Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}
