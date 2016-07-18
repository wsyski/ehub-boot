/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

/**
 * Indicates that an exception occurred in the eHUB.
 * 
 * <p>
 * It is intended for the client of the eHUB. For exceptions within the eHUB see the {@link EhubRuntimeException} and
 * all its sub-classes.
 * </p>
 */
public final class EhubException extends Exception implements IEhubException {
    private final EhubError ehubError;

    /**
     * Constructs a new {@link EhubException}.
     * 
     * @param ehubError the {@link EhubError}
     */
    public EhubException(EhubError ehubError) {
        super(ehubError.getMessage());
        this.ehubError = ehubError;
    }

    /**
     * Returns the {@link EhubError}.
     * 
     * @return the {@link EhubError}
     */
    public EhubError getEhubError() {
        return ehubError;
    }
}
