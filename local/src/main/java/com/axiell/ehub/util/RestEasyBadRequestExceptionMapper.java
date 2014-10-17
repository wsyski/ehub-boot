/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.axiell.ehub.*;
import org.jboss.resteasy.spi.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.security.UnauthorizedException;

/**
 * An {@link ExceptionMapper} that maps a {@link BadRequestException} to a {@link Response} with status
 * {@link Status#BAD_REQUEST}, except when the cause of the {@link BadRequestException} is an
 * {@link UnauthorizedException}. Then the {@link EhubRuntimeExceptionMapper} is used.
 */
@Provider
public final class RestEasyBadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestEasyBadRequestExceptionMapper.class);

    /**
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(final BadRequestException exception) {
        final Throwable cause = exception.getCause();

        if (cause instanceof UnauthorizedException) {
            final UnauthorizedException ehubException = (UnauthorizedException) cause;
            return handleEhubRuntimeException(ehubException);
        } else if (cause instanceof ForbiddenException) {
            final ForbiddenException ehubException = (ForbiddenException) cause;
            return handleEhubRuntimeException(ehubException);
        } else if (cause instanceof InternalServerErrorException) {
            final InternalServerErrorException ehubException = (InternalServerErrorException) cause;
            return handleEhubRuntimeException(ehubException);
        } else {
            LOGGER.error(exception.getMessage(), exception);
            final EhubError ehubError = ErrorCause.BAD_REQUEST.toEhubError();
            return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(ehubError).build();
        }
    }

    private Response handleEhubRuntimeException(EhubRuntimeException ehubException) {
        final EhubRuntimeExceptionMapper<EhubRuntimeException> ehubRuntimeExceptionMapper = new EhubRuntimeExceptionMapper<>();
        return ehubRuntimeExceptionMapper.toResponse(ehubException);
    }
}
