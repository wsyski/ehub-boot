package com.axiell.ehub.local.lms.arena.exception;

import jakarta.ws.rs.WebApplicationException;

public class RestApiRuntimeException extends WebApplicationException {

    public RestApiRuntimeException(final String s, final Throwable throwable) {
        super(s, throwable);
    }

    public RestApiRuntimeException(final String s) {
        super(s);
    }
}
