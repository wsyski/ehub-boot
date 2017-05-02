package com.axiell.authinfo;

public class AuthInfoRuntimeException extends RuntimeException {

    public AuthInfoRuntimeException(final String s, final Throwable throwable) {
        super(s, throwable);
    }

    public AuthInfoRuntimeException(final String s) {
        super(s);
    }
}