package com.axiell.ehub.security;

public class AuthInfoSecretKeyResolver implements IAuthInfoSecretKeyResolver {
    private static final String SECRET_KEY = "c2VjcmV0S2V5";

    @Override
    public String getSecretKey(long ehubConsumerId) {
        return SECRET_KEY;
    }
}
