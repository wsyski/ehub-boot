package com.axiell.ehub.security;

public class AuthInfoSecretKeyResolver implements IAuthInfoSecretKeyResolver {
    private String secretKey;

    public AuthInfoSecretKeyResolver(final String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String getSecretKey(long ehubConsumerId) {
        return secretKey;
    }
}
