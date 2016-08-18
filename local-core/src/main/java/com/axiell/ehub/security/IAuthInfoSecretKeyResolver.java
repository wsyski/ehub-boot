package com.axiell.ehub.security;

public interface IAuthInfoSecretKeyResolver {

    String getSecretKey(long ehubConsumerId);
}
