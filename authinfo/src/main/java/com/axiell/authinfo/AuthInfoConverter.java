package com.axiell.authinfo;

import javax.ws.rs.ext.ParamConverter;

public class AuthInfoConverter implements ParamConverter<AuthInfo> {

    private IAuthInfoResolver authInfoResolver;

    public AuthInfoConverter(final IAuthInfoResolver authInfoResolver) {
        this.authInfoResolver = authInfoResolver;
    }

    @Override
    public AuthInfo fromString(final String authorizationHeader) {
        return authInfoResolver.resolve(authorizationHeader);
    }

    @Override
    public String toString(final AuthInfo authInfo) {
        return authInfoResolver.serialize(authInfo);
    }
}
