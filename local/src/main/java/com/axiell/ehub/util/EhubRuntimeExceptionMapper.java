/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.axiell.ehub.EhubRuntimeException;

/**
 * An {@link ExceptionMapper} that maps all {@link EhubRuntimeException}s to the {@link Response} of the {@link EhubRuntimeException}.
 */
@Provider
public final class EhubRuntimeExceptionMapper<E extends EhubRuntimeException> implements ExceptionMapper<E> {
    private static final Logger LOGGER = Logger.getLogger(EhubRuntimeExceptionMapper.class);

    /**
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public final Response toResponse(E exception) {
        LOGGER.error(exception.getMessage(), exception);        
        return exception.getResponse();
    }
}
