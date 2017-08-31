package com.axiell.authinfo;

public class InvalidAuthorizationHeaderSignatureRuntimeException extends AuthInfoRuntimeException {

    public InvalidAuthorizationHeaderSignatureRuntimeException(Throwable ex) {
        super("Invalid Authorization Header Signature", ex);
    }

    public InvalidAuthorizationHeaderSignatureRuntimeException() {
        super("Invalid Authorization Header Signature");
    }
}