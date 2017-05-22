package com.axiell.authinfo;

public interface IAuthHeaderSecretKeyResolver {

    String getSecretKey(AuthInfo authInfo);

    long getExpirationTimeInSeconds(AuthInfo authInfo);

    boolean isValidate();

    boolean isDebug();
}
