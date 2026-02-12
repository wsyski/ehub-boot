package com.axiell.ehub.local.lms.arena.client;

import com.axiell.ehub.local.lms.arena.error.ArenaLocalErrorCause;
import com.axiell.ehub.local.lms.arena.error.ArenaLocalRestApiError;
import com.axiell.ehub.local.lms.arena.exception.RestApiException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Aspect
@Component
public class ArenaLocalClientExceptionAspect {

    private static WebApplicationException unwrapException(WebApplicationException ex) {
        return ex.getCause() == null ? ex : (WebApplicationException) ex.getCause();
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.local.lms.arena.client.ArenaLocalClient+.*(..))", throwing = "pe")
    public void toEhubException(final JoinPoint joinPoint, final ProcessingException pe) throws RestApiException {
        log.error(pe.getMessage(), pe);
        throw new RestApiException(ArenaLocalErrorCause.BAD_REQUEST.toError(Collections.singletonMap(ArenaLocalRestApiError.ARGUMENT_THROWABLE_CLASS_NAME, pe.getClass().getName())));
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.local.lms.arena.client.ArenaLocalClient+.*(..))", throwing = "wae")
    public void toRestApiException(final JoinPoint joinPoint, final WebApplicationException wae) throws RestApiException {
        WebApplicationException unwrappedException = unwrapException(wae);
        log.info("WebApplicationException: {}", unwrappedException.getMessage());
        final Response response = unwrappedException.getResponse();
        ArenaLocalRestApiError restApiError;
        try {
            restApiError = response.readEntity(ArenaLocalRestApiError.class);
        } catch (Exception ex) {
            log.error("Failed to read error response entity", ex);
            throw new RestApiException(ArenaLocalErrorCause.BAD_REQUEST.toError(Collections.singletonMap(ArenaLocalRestApiError.ARGUMENT_THROWABLE_CLASS_NAME, unwrappedException.getClass().getName())));
        }
        throw new RestApiException(restApiError);
    }
}
