package com.axiell.ehub.security;

public interface IAuthHeaderSecretKeyResolver {

    String getSecretKey(Long ehubConsumerId);

    boolean isValidateSignature();
}
