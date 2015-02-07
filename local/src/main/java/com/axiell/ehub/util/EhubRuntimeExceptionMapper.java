/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import com.axiell.ehub.EhubRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * An {@link ExceptionMapper} that maps all {@link EhubRuntimeException}s to the {@link Response} of the {@link EhubRuntimeException}.
 */
@Provider
public final class EhubRuntimeExceptionMapper extends AbstractEhubExceptionMapper<EhubRuntimeException> implements ExceptionMapper<EhubRuntimeException> {

    /**
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(EhubRuntimeException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return exception.getResponse(getMediaType());
    }
}
