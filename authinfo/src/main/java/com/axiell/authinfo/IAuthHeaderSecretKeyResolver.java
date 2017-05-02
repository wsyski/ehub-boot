package com.axiell.authinfo;

public interface IAuthHeaderSecretKeyResolver {

    String getSecretKey(Long id);

    boolean isValidate();

    long getExpirationTimeInSeconds();
}
