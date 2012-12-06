/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Skeletal implementation of the eHUB {@link RuntimeException}. It should be sub-classed to provide the specific
 * exceptions.
 */
public abstract class EhubRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -3317131212182853837L;
    private final int status;
    private final EhubError ehubError;
    private final String message;
    private final Set<Header> headers = new HashSet<>();

    /**
     * Constructs a new {@link EhubRuntimeException}.
     * 
     * @param status the HTTP status code
     * @param cause the cause
     */
    protected EhubRuntimeException(final int status, final ErrorCause cause) {
        super();
        this.status = status;
        this.ehubError = cause.toEhubError();
        this.message = ehubError.getMessage();
    }

    /**
     * Constructs a new {@link EhubRuntimeException}.
     * 
     * @param status the HTTP status code
     * @param message the formatted error message to be written in the server log
     * @param cause the cause
     */
    protected EhubRuntimeException(final int status, final String message, final ErrorCause cause) {
        super();
        this.status = status;
        this.ehubError = cause.toEhubError();
        this.message = message;
    }

    /**
     * Constructs a new {@link EhubRuntimeException}.
     * 
     * @param status the HTTP status code
     * @param message the formatted error message to be written in the server log
     * @param throwable the root cause of the exception
     * @param cause the cause
     */
    protected EhubRuntimeException(final int status, final String message, Throwable throwable, final ErrorCause cause) {
        super(throwable);
        this.status = status;
        this.ehubError = cause.toEhubError();
        this.message = message;
    }

    /**
     * Constructs a new {@link EhubRuntimeException}.
     * 
     * @param status the HTTP status code
     * @param cause the cause
     * @param arguments an array of {@link ErrorCauseArgument}s
     */
    protected EhubRuntimeException(final int status, final ErrorCause cause, final ErrorCauseArgument... arguments) {
        super();
        this.status = status;
        this.ehubError = cause.toEhubError(arguments);
        this.message = ehubError.getMessage();
    }

    /**
     * Constructs a new {@link EhubRuntimeException}.
     * 
     * @param status the HTTP status code
     * @param message the formatted error message to be written in the server log
     * @param cause the cause
     * @param arguments an array of {@link ErrorCauseArgument}s
     */
    protected EhubRuntimeException(final int status, final String message, final ErrorCause cause, final ErrorCauseArgument... arguments) {
        super();
        this.status = status;
        this.ehubError = cause.toEhubError(arguments);
        this.message = message;
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public final String getMessage() {
        return message;
    }

    /**
     * Returns a {@link Response}.
     * 
     * @return a {@link Response} with the provided status, content type {@link MediaType#APPLICATION_XML} and an
     * {@link EhubError} as entity
     */
    public final Response getResponse() {
        final Response.ResponseBuilder builder = Response.status(status).type(MediaType.APPLICATION_XML).entity(ehubError);

        for (Header header : headers) {
            final String name = header.name;
            final String value = header.value;
            builder.header(name, value);
        }

        return builder.build();
    }

    /**
     * Returns the {@link EhubError}.
     *
     * @return the {@link EhubError}
     */
    public final EhubError getEhubError() {
        return ehubError;
    }
    
    /**
     * Adds a HTTP header to be included in the reponse.
     * 
     * @param name the name of the header
     * @param value the value of the header
     */
    protected final void addHeader(final String name, final String value) {
        final Header header = new Header();
        header.name = name;
        header.value = value;
        headers.add(header);
    }

    /**
     * Represents a HTTP header.
     */
    private static class Header {
        private String name;
        private String value;
    }
}
