/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.resteasy.client.exception.ResteasyClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Aspect converts exceptions thrown by the {@link EhubClient} to {@link EhubException}s.
 */
@Aspect
public class EhubClientExceptionAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(EhubClientExceptionAspect.class);

    /**
     * Converts the {@link Throwable} to an {@link EhubException}.
     *
     * @param rce the {@link ResteasyClientException} to convert
     * @throws EhubException when this method is invoked
     */
    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.EhubClient.*(..))", throwing = "rce")
    public void toEhubException(final JoinPoint joinPoint, final ResteasyClientException rce) throws EhubException {
        LOGGER.error(rce.getMessage(), rce);
        throw new EhubException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
    }

    /**
     * Converts the {@link EhubRuntimeException} to an {@link EhubException}.
     *
     * @param ere the {@link EhubRuntimeException} to convert
     * @throws EhubException when this method is invoked
     */
    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.EhubClient.*(..))", throwing = "ere")
    public void toEhubException(final JoinPoint joinPoint, final EhubRuntimeException ere) throws EhubException {
        final EhubError ehubError = ere.getEhubError();
        throw new EhubException(ehubError);
    }
}
