/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

/**
 * Indicates that a requested part of the eHUB has not been implemented. It will result in an error response with status
 * code 501.
 */
public final class NotImplementedException extends EhubRuntimeException {
    private static final long serialVersionUID = -1940249514789590986L;
    private static final int NOT_IMPLEMENTED_STATUS_CODE = 501;

    /**
     * Constructs a new {@link NotImplementedException}.
     *  
     * @param message the formatted error message to be written in the server log
     */
    public NotImplementedException(String message) {
        super(NOT_IMPLEMENTED_STATUS_CODE, message, ErrorCause.NOT_IMPLEMENTED);
    }
}
