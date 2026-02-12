package com.axiell.ehub.client;

import com.axiell.ehub.common.EhubError;
import com.axiell.ehub.common.EhubWebApplicationException;
import com.axiell.ehub.common.ErrorCause;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * This Aspect converts exceptions thrown by the {@link EhubServiceClient} to {@link EhubWebApplicationException}s.
 */
@Slf4j
@Aspect
@Component
public class EhubServiceClientExceptionAspect {

    private static WebApplicationException unwrapException(WebApplicationException ex) {
        return ex.getCause() == null ? ex : (WebApplicationException) ex.getCause();
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.client.IEhubServiceClient.*(..))", throwing = "pe")
    public void toEhubException(final JoinPoint joinPoint, final ProcessingException pe) throws EhubWebApplicationException {
        log.error(pe.getMessage(), pe);
        throw new EhubWebApplicationException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.client.IEhubServiceClient.*(..))", throwing = "wae")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final WebApplicationException wae) throws EhubWebApplicationException {
        WebApplicationException unwrappedException = unwrapException(wae);
        log.info("WebApplicationException: {}", unwrappedException.getMessage());
        final Response response = unwrappedException.getResponse();
        throw getEhubException(response);
    }

    private EhubWebApplicationException getEhubException(final Response response) {
        try {
            EhubError ehubError = response.readEntity(EhubError.class);
            return new EhubWebApplicationException(ehubError);
        } catch (Exception ex) {
            log.error("Failed to read error response entity", ex);
            return new EhubWebApplicationException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
        }
    }
}
