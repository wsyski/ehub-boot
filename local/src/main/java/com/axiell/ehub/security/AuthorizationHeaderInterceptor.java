/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import com.axiell.ehub.ErrorCause;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Provider
@Priority(Integer.MIN_VALUE)
public final class AuthorizationHeaderInterceptor implements ContainerRequestFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        if (requestContext instanceof PostMatchContainerRequestContext) {
            ResourceMethodInvoker resourceMethodInvoker = ((PostMatchContainerRequestContext) requestContext).getResourceMethod();
            Method method = resourceMethodInvoker.getMethod();
            if (methodParametersContainAuthInfo(method)) {
                final String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
                if (authorization == null || authorization.isEmpty()) {
                    throw new UnauthorizedException(
                            "The method '" + method.getName() + "' in the resource '" + resourceMethodInvoker.getResourceClass()
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
