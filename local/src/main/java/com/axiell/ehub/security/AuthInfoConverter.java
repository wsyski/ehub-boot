/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import org.jboss.resteasy.spi.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.ext.Provider;

/**
 * Provides the possibility to convert a string to an {@link AuthInfo} and an
 * {@link AuthInfo} to a string.
 */
@Provider
public class AuthInfoConverter implements StringConverter<AuthInfo> {

    @Autowired(required = true)
    private IAuthInfoResolver authInfoResolver;

    @Override
    public AuthInfo fromString(final String authorizationHeader) {
        return authInfoResolver.resolve(authorizationHeader);
    }

    @Override
    public String toString(final AuthInfo authInfo) {
        return authInfo.toString();
    }
}
