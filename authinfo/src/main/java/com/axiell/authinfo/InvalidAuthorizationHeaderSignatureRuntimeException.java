package com.axiell.authinfo;

public class InvalidAuthorizationHeaderSignatureRuntimeException extends AuthInfoRuntimeException {

    public InvalidAuthorizationHeaderSignatureRuntimeException() {
        super("Invalid Authorization Header Signature");
    }
}