/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

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
        return authInfo.toString();
    }
}
