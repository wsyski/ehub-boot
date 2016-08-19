package com.axiell.ehub.security;

public class AuthInfoSecretKeyResolver implements IAuthInfoSecretKeyResolver {
    private String secretKey;
    private boolean isValidate;

    public AuthInfoSecretKeyResolver(final String secretKey, final boolean isValidate) {
        this.secretKey = secretKey;
        this.isValidate = isValidate;
    }

    @Override
    public String getSecretKey(long ehubConsumerId) {
        return secretKey;
    }

    @Override
    public boolean isValidate() {
        return isValidate;
    }
}
