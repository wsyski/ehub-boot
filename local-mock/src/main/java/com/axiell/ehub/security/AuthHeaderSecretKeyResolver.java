package com.axiell.ehub.security;

public class AuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
    private String secretKey;

    public AuthHeaderSecretKeyResolver(final String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String getSecretKey(Long ehubConsumerId) {
        return secretKey;
    }

    @Override
    public boolean isValidateSignature() {
        return true;
    }
}
