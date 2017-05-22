package com.axiell.authinfo;

import org.springframework.beans.factory.annotation.Required;

public class ConstantAuthHeaderSecretKeyResolver extends AbstractAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
    private String secretKey;
    private long expirationTimeInSeconds;

    @Override
    public String getSecretKey(final AuthInfo authInfo) {
        return this.secretKey;
    }

    @Override
    public long getExpirationTimeInSeconds(final AuthInfo authInfo) {
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
}
