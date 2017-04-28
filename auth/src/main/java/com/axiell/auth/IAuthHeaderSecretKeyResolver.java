package com.axiell.auth;

public interface IAuthHeaderSecretKeyResolver {

    String getSecretKey(Long ehubConsumerId);
}
