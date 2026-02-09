/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * This Aspect converts exceptions thrown by the {@link EhubClient} to {@link EhubException}s.
 */
@Slf4j
@Aspect
@EnableAspectJAutoProxy
@Component
public class EhubClientExceptionAspect {

    private static WebApplicationException unwrapException(WebApplicationException ex) {
        return ex.getCause() == null ? ex : (WebApplicationException) ex.getCause();
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.IEhubService.*(..))", throwing = "pe")
    public void toEhubException(final JoinPoint joinPoint, final ProcessingException pe) throws EhubException {
        log.error(pe.getMessage(), pe);
        throw new EhubException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.IEhubService.*(..))", throwing = "wae")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final WebApplicationException wae) throws EhubException {
        WebApplicationException unwrappedException = unwrapException(wae);
        log.info("WebApplicationException: {}", unwrappedException.getMessage());
        final Response response = unwrappedException.getResponse();
        throw getEhubException(response);
    }

    private EhubException getEhubException(final Response response) {
        try {
            EhubError ehubError = response.readEntity(EhubError.class);
            return new EhubException(ehubError);
        } catch (Exception ex) {
            log.error("Failed to read error response entity", ex);
            return new EhubException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
        }
    }
}
