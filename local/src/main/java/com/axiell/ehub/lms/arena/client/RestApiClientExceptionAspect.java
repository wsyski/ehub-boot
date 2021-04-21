package com.axiell.ehub.lms.arena.client;

import com.axiell.ehub.lms.arena.error.ErrorCause;
import com.axiell.ehub.lms.arena.error.RestApiError;
import com.axiell.ehub.lms.arena.exception.RestApiException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.resteasy.client.exception.ResteasyClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;

@Aspect
public class RestApiClientExceptionAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiClientExceptionAspect.class);

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.lms.arena.client.LocalRestApiClient+.*(..))", throwing = "rce")
    public void toRestaApiException(final JoinPoint joinPoint, final ResteasyClientException rce) throws RestApiException {
        LOGGER.error(rce.getMessage(), rce);
        throw new RestApiException(ErrorCause.BAD_REQUEST.toError(Collections.singletonMap("throwableClassName", rce.getClass().getName())));
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.lms.arena.client.LocalRestApiClient+.*(..))", throwing = "cee")
    public void toBadRequestErrorException(final JoinPoint joinPoint, final WebApplicationException cee) throws RestApiException {
        LOGGER.error(cee.getMessage(), cee);
        final Response response = cee.getResponse();
        throw getRestApiException(response);
    }

    private RestApiException getRestApiException(final Response response) {
        RestApiError restApiError = response.readEntity(RestApiError.class);
        return new RestApiException(restApiError);
    }
}
