package com.axiell.auth.util;

public class AuthRuntimeException extends RuntimeException {

    public AuthRuntimeException(final String s, final Throwable throwable) {
        super(s, throwable);
    }

    public AuthRuntimeException(final String s) {
        super(s);
    }
}