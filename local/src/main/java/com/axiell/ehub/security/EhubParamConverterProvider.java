/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class EhubParamConverterProvider implements ParamConverterProvider {

    private IAuthInfoResolver authInfoResolver;

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        if (rawType.getName().equals(AuthInfo.class.getName())) {
            return (ParamConverter<T>) new AuthInfoConverter(authInfoResolver);
        }
        return null;
    }

    @Required
    public void setAuthInfoResolver(final IAuthInfoResolver authInfoResolver) {
        this.authInfoResolver = authInfoResolver;
    }
}
