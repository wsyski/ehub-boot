package com.axiell.authinfo;

public class MissingOrUnparseableAuthorizationHeaderRuntimeException extends AuthInfoRuntimeException {

    public MissingOrUnparseableAuthorizationHeaderRuntimeException() {
        super("Missing or unparseable Authorization Header");
    }
}