/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import jakarta.ws.rs.core.Response.Status;

/**
 * Indicates that a requested resource could not be found. It will result in an error response with status code 404.  
 */
public final class NotFoundException extends EhubRuntimeException {
    private static final long serialVersionUID = 4489266128948452712L;

    /**
     * Constructs a new {@link NotFoundException}.
     * 
     * @param cause the {@link ErrorCause}
     * @param arguments an array of {@link ErrorCauseArgument}s
     */
    public NotFoundException(ErrorCause cause, ErrorCauseArgument... arguments) {
        super(Status.NOT_FOUND.getStatusCode(), cause, arguments);
    }
}
