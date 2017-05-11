package com.axiell.authinfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ConstantAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
    private String secretKey;
    private boolean isValidate = true;
    private long expirationTimeInSeconds;

    @Override
    public String getSecretKey(final Long ehubConsumerId) {
        if (StringUtils.isBlank(secretKey)) {
            throw new MissingSecretKeyRuntimeException();
        }
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

    public void setValidate(boolean validate) {
        isValidate = validate;
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
