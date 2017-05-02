package com.axiell.authinfo;

public class MissingSecretKeyRuntimeException extends AuthInfoRuntimeException {

    public MissingSecretKeyRuntimeException() {
        super("Missing or blank secret key");
    }
}