/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import javax.ws.rs.core.Response.Status;

/**
 * Indicates that a bad request was received by the eHUB. It will result in an error response with status code 400. 
 */
public final class BadRequestException extends EhubRuntimeException {
    private static final long serialVersionUID = -7157507678767184184L;

    /**
     * Constructs a new {@link BadRequestException}.
     * 
     * @param cause the cause
     */
    public BadRequestException(final ErrorCause cause) {
        super(Status.BAD_REQUEST.getStatusCode(), cause);
    }
}
