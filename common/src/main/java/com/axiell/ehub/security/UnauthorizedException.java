/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import com.axiell.ehub.EhubRuntimeException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;

import jakarta.ws.rs.core.Response.Status;

/**
 * Indicates that the request requires user authentication. It will result in an error response with status code 401.
 */
public final class UnauthorizedException extends EhubRuntimeException {

    /**
     * Constructs a new {@link UnauthorizedException}.
     *
     * @param cause the cause
     */
    public UnauthorizedException(final ErrorCause cause) {
        this(cause.toEhubError().getMessage(), cause);
    }

    /**
     * Constructs a new {@link UnauthorizedException}.
     *
     * @param message the formatted error message to be written in the server log
     * @param cause   the cause
     */
    public UnauthorizedException(final String message, final ErrorCause cause) {
        super(Status.UNAUTHORIZED.getStatusCode(), message, cause);
        addHeader("WWW-Authenticate", "eHUB");
    }

    /**
     * Constructs a new {@link UnauthorizedException}.
     *
     * @param cause     the cause
     * @param arguments an array of {@link ErrorCauseArgument}s
     */
    public UnauthorizedException(final ErrorCause cause, final ErrorCauseArgument... arguments) {
        super(Status.UNAUTHORIZED.getStatusCode(), cause, arguments);
    }
}
