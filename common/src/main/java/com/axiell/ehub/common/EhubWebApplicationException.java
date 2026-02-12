/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.common;

import jakarta.ws.rs.WebApplicationException;
import lombok.Getter;

/**
 * Indicates that an exception occurred in the eHUB.
 *
 * <p>
 * It is intended for the client of the eHUB. For exceptions within the eHUB see the {@link EhubRuntimeException} and
 * all its sub-classes.
 * </p>
 */
@Getter
public final class EhubWebApplicationException extends WebApplicationException implements IEhubException {
    private final EhubError ehubError;

    /**
     * Constructs a new {@link EhubWebApplicationException}.
     *
     * @param ehubError the {@link EhubError}
     */
    public EhubWebApplicationException(final EhubError ehubError) {
        super(ehubError.getMessage());
        this.ehubError = ehubError;
    }
}
