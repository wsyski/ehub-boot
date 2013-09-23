/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.Precedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import com.axiell.ehub.ErrorCause;

/**
 * This {@link PreProcessInterceptor} is executed before all resource methods that contains a parameter of type
 * {@link AuthInfo}. It makes sure that the request contains an <code>Authorization</code> header, if it doesn't an
 * {@link UnauthorizedException} is thrown.
 */
@Provider
@Precedence(value = "SECURITY")
@ServerInterceptor
final class AuthorizationHeaderInterceptor implements PreProcessInterceptor, AcceptedByMethod {

    /**
     * @see org.jboss.resteasy.spi.interception.AcceptedByMethod#accept(java.lang.Class, java.lang.reflect.Method)
     */
    @Override
    public boolean accept(@SuppressWarnings("rawtypes") Class c, Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final List<Class<?>> parameterTypeList = Arrays.asList(parameterTypes);
        return parameterTypeList.contains(AuthInfo.class);
    }

    /**
     * @see org.jboss.resteasy.spi.interception.PreProcessInterceptor#preProcess(org.jboss.resteasy.spi.HttpRequest,
     * org.jboss.resteasy.core.ResourceMethod)
     */
    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethod resourceMethod) throws Failure, WebApplicationException {
        final HttpHeaders headers = request.getHttpHeaders();
        final List<String> authorizationHeaders = headers.getRequestHeader("Authorization");

        if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
            throw new UnauthorizedException("The method '" + resourceMethod.getMethod().getName() + "' in the resource '" + resourceMethod.getResourceClass()
                    + "'expects an 'AuthInfo', but the request does not contain an Authorization header", ErrorCause.MISSING_AUTHORIZATION_HEADER);
        }

        return null;
    }
}
