/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.InternalServerErrorException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * This Aspect converts {@link ClientErrorException}s thrown by the {@link IContentProviderDataAccessor}s to
 * {@link InternalServerErrorException}s.
 */
@Aspect
public class ContentProviderResponseFailureAspect extends AbstractContentProviderResponseFailureAspect {

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.provider.IContentProviderDataAccessor.*(..))", throwing = "cee")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final WebApplicationException cee) {
        final Response response = cee.getResponse();
        throw getContentProviderException(response, joinPoint);
    }

    private InternalServerErrorException getContentProviderException(final Response response, final JoinPoint joinPoint) {
        IContentProviderExceptionFactory contentProviderExceptionFactory = getContentProviderErrorExceptionFactory(joinPoint);
        return contentProviderExceptionFactory.create(response);
    }
}