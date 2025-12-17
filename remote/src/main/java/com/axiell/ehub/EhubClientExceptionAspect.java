/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * This Aspect converts exceptions thrown by the {@link EhubClient} to {@link EhubException}s.
 */
@Aspect
public class EhubClientExceptionAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(EhubClientExceptionAspect.class);

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.IEhubService.*(..))", throwing = "pe")
    public void toEhubException(final JoinPoint joinPoint, final ProcessingException pe) throws EhubException {
        LOGGER.error(pe.getMessage(), pe);
        throw new EhubException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.IEhubService.*(..))", throwing = "wae")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final WebApplicationException wae) throws EhubException {
        LOGGER.error("WebApplicationException: {}", wae.getMessage(), wae);
        final Response response = wae.getResponse();
        throw getEhubException(response);
    }

    private EhubException getEhubException(final Response response) {
        try {
            EhubError ehubError = response.readEntity(EhubError.class);
            return new EhubException(ehubError);
        } catch (Exception e) {
            LOGGER.error("Failed to read error response entity", e);
            return new EhubException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
        }
    }
}