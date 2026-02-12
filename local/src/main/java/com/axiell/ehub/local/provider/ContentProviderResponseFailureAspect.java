package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.InternalServerErrorException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * This Aspect converts {@link ClientErrorException}s thrown by the {@link IContentProviderDataAccessor}s to
 * {@link InternalServerErrorException}s.
 */
@Slf4j
@Aspect
@Component
public class ContentProviderResponseFailureAspect extends AbstractContentProviderResponseFailureAspect {

    private static WebApplicationException unwrapException(WebApplicationException ex) {
        return ex.getCause() == null ? ex : (WebApplicationException) ex.getCause();
    }

    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.local.provider.IContentProviderDataAccessor.*(..))", throwing = "wae")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final WebApplicationException wae) {
        WebApplicationException unwrappedException = unwrapException(wae);
        log.info("WebApplicationException: {}", unwrappedException.getMessage());
        final Response response = unwrappedException.getResponse();
        throw getContentProviderException(response, joinPoint);
    }

    private InternalServerErrorException getContentProviderException(final Response response, final JoinPoint joinPoint) {
        final IContentProviderExceptionFactory contentProviderExceptionFactory = getContentProviderErrorExceptionFactory(joinPoint);
        return contentProviderExceptionFactory.create(response);
    }
}
