/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.InternalServerErrorException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;

/**
 * This Aspect converts {@link ClientResponseFailure}s thrown by the {@link IContentProviderDataAccessor}s to
 * {@link InternalServerErrorException}s.
 */
@Aspect
public class LegacyContentProviderResponseFailureAspect extends AbstractContentProviderResponseFailureAspect {
    /**
     * Converts the {@link ClientResponseFailure} to an {@link InternalServerErrorException}.
     *
     * @param joinPoint the {@link JoinPoint} to use
     * @param crf       the {@link ClientResponseFailure} to convert
     * @throws InternalServerErrorException when this method is invoked
     */
    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.provider.IContentProviderDataAccessor.*(..))", throwing = "crf")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final ClientResponseFailure crf) {
        final ClientResponse<?> response = crf.getResponse();
        throw getContentProviderException(response, joinPoint);
    }

    private InternalServerErrorException getContentProviderException(final ClientResponse<?> response, final JoinPoint joinPoint) {
        response.resetStream();
        IContentProviderExceptionFactory contentProviderExceptionFactory = getContentProviderErrorExceptionFactory(joinPoint);
        return contentProviderExceptionFactory.create(response);
    }
}
