package com.axiell.auth;

import org.springframework.beans.factory.annotation.Required;

public class JwtAuthHeaderParser implements IAuthHeaderParser {

    private IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver;

    @Override
    public AuthInfo parse(String value) {
        return null;
    }

    @Override
    public String serialize(AuthInfo authInfo) {
        return null;
    }

    @Required
    public void setAuthHeaderSecretKeyResolver(final IAuthHeaderSecretKeyResolver authHeaderSecretKeyResolver) {
        this.authHeaderSecretKeyResolver = authHeaderSecretKeyResolver;
    }
}
