/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import javax.ws.rs.core.Response.Status;

/**
 * Indicates that an unexpected exception occurred within the eHUB. It will result in an error response with status code
 * 500.
 */
public final class InternalServerErrorException extends EhubRuntimeException {
    private static final long serialVersionUID = -5479730041071582555L;

    /**
     * Constructs a new {@link InternalServerErrorException}.
     * 
     * @param message the formatted error message to be written in the server log
     */
    public InternalServerErrorException(String message) {
        super(Status.INTERNAL_SERVER_ERROR.getStatusCode(), message, ErrorCause.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructs a new {@link InternalServerErrorException}.
     * 
     * @param message the formatted error message to be written in the server log
     * @param throwable the root cause of the exception
     */
    public InternalServerErrorException(String message, Throwable throwable) {
        super(Status.INTERNAL_SERVER_ERROR.getStatusCode(), message, throwable, ErrorCause.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructs a new {@link InternalServerErrorException}.
     * 
     * @param message the formatted error message to be written in the server log
     * @param cause the {@link ErrorCause}
     * @param arguments an array of {@link ErrorCauseArgument}s
     */
    public InternalServerErrorException(String message, ErrorCause cause, ErrorCauseArgument... arguments) {
        super(Status.INTERNAL_SERVER_ERROR.getStatusCode(), message, cause, arguments);
    }
}
