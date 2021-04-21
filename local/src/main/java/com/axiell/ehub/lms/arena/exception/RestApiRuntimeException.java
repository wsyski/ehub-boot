package com.axiell.ehub.lms.arena.exception;

import javax.ws.rs.WebApplicationException;

public class RestApiRuntimeException extends WebApplicationException {

    public RestApiRuntimeException(final String s, final Throwable throwable) {
        super(s, throwable);
    }

    public RestApiRuntimeException(final String s) {
        super(s);
    }
}
