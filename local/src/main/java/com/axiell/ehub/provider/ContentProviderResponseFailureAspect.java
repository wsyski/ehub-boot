/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
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
        final ContentProvider contentProvider = getContentProvider(joinPoint);
        final String message = getMessage(contentProvider, response);
        final ErrorCauseArgument nameArg = makeContentProviderNameErrorCauseArgument(contentProvider);
        final ErrorCauseArgument statusArg = makeStatusCodeErrorCauseArgument(response);
        throw new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, nameArg, statusArg);
    }

    private ErrorCauseArgument makeStatusCodeErrorCauseArgument(final Response response) {
        final int statusCode = getStatusCode(response);
        return new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, statusCode);
    }

    private int getStatusCode(final Response response) {
        final Response.StatusType status = response.getStatusInfo();
        return status == null ? UNKNOWN_STATUS_CODE : status.getStatusCode();
    }

    private String getMessage(final ContentProvider contentProvider, final Response response) {
        final IContentProviderErrorResponseBodyReader reader = ContentProviderErrorResponseBodyReaderFactory.create(contentProvider);
        final String message = reader.read(response);
        if (message == null)
            return DEFAULT_MESSAGE;
        else
            return message;
    }
}
