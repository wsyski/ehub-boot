package com.axiell.auth;

public interface IAuthHeaderSecretKeyResolver {

    String getSecretKey(Long id);

    boolean isValidate();

    long getExpirationTimeInSeconds();
}
