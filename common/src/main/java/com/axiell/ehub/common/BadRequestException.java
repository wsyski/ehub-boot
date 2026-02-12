/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.common;

import jakarta.ws.rs.core.Response.Status;

/**
 * Indicates that a bad request was received by the eHUB. It will result in an error response with status code 400.
 */
public class BadRequestException extends EhubRuntimeException {

    /**
     * Constructs a new {@link BadRequestException}.
     *
     * @param cause the cause
     */
    public BadRequestException(final ErrorCause cause) {
        super(Status.BAD_REQUEST.getStatusCode(), cause);
    }

    public BadRequestException(ErrorCause cause, ErrorCauseArgument... arguments) {
        super(Status.BAD_REQUEST.getStatusCode(), cause, arguments);
    }
}
