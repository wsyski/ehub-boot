/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * This Aspect converts exceptions thrown by the {@link EhubClient} to {@link EhubException}s.
 */
@Aspect
public class EhubClientExceptionAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(EhubClientExceptionAspect.class);

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.IEhubService.*(..))", throwing = "rce")
    public void toEhubException(final JoinPoint joinPoint, final ResteasyWebApplicationException rce) throws EhubException {
        LOGGER.error(rce.getMessage(), rce);
        throw new EhubException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.IEhubService.*(..))", throwing = "cee")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final WebApplicationException cee) throws EhubException {
        final Response response = cee.getResponse();
        throw getEhubException(response);
    }

    private EhubException getEhubException(final Response response) {
        EhubError ehubError=response.readEntity(EhubError.class);
        return new EhubException(ehubError);
    }
}
