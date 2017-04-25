/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import com.axiell.auth.AuthInfo;
import com.axiell.ehub.ErrorCause;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Provider
@Priority(Integer.MIN_VALUE)
public final class AuthorizationHeaderInterceptor implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method != null) {
            if (methodParametersContainAuthInfo(method)) {
                final String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
                if (authorization == null || authorization.isEmpty()) {
                    throw new UnauthorizedException(
                            "The method '" + method.getName() + "' in the resource '" + resourceInfo.getResourceClass()
                                    + "'expects an 'AuthInfo', but the request does not contain an Authorization header",
                            ErrorCause.MISSING_AUTHORIZATION_HEADER);
                }
            }
        }
    }

    private boolean methodParametersContainAuthInfo(final Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return Arrays.stream(parameterTypes).anyMatch(clazz -> clazz.equals(AuthInfo.class));
    }
}
