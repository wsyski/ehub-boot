package com.axiell.ehub;

import jakarta.ws.rs.core.Response.Status;

/**
 * Indicates that action has been denied.
 */
public class ForbiddenException extends EhubRuntimeException {
    private static final long serialVersionUID = -1940249514789590986L;

    /**
     * Constructs a new {@link ForbiddenException}.
     *
     * @param cause the {@link ErrorCause}
     * @param arguments an array of {@link ErrorCauseArgument}s
     */
    public ForbiddenException(ErrorCause cause, ErrorCauseArgument... arguments) {
        super(Status.FORBIDDEN.getStatusCode(), cause, arguments);
    }
}
