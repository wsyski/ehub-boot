package com.axiell.auth;

public class ConstantAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
    private String secretKey;

    public ConstantAuthHeaderSecretKeyResolver(final String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String getSecretKey(final Long ehubConsumerId) {
        return this.secretKey;
    }

    @Override
    public boolean isValidateSignature() {
        return true;
    }
}
