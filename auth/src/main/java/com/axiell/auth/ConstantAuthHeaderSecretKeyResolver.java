package com.axiell.auth;

import org.springframework.beans.factory.annotation.Required;

public class ConstantAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
    private String secretKey;
    private boolean isValidate;
    private long expirationTimeInSeconds;

    @Override
    public String getSecretKey(final Long ehubConsumerId) {
        return this.secretKey;
    }

    @Override
    public boolean isValidate() {
        return isValidate;
    }

    @Override
    public long getExpirationTimeInSeconds() {
        return expirationTimeInSeconds;
    }

    @Required
    public void setExpirationTimeInSeconds(final long expirationTimeInSeconds) {
        this.expirationTimeInSeconds = expirationTimeInSeconds;
    }

    @Required
    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    @Required
    public void setValidate(boolean validate) {
        isValidate = validate;
    }
}
