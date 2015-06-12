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
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;

import javax.ws.rs.core.Response.Status;

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
        final ContentProvider contentProvider = getContentProvider(joinPoint);
        final String message = getMessage(contentProvider, response);
        final ErrorCauseArgument nameArg = makeContentProviderNameErrorCauseArgument(contentProvider);
        final ErrorCauseArgument statusArg = makeStatusCodeErrorCauseArgument(response);
        throw new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, nameArg, statusArg);
    }

    private ErrorCauseArgument makeStatusCodeErrorCauseArgument(final ClientResponse<?> response) {
        final int statusCode = getStatusCode(response);
        return new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, statusCode);
    }

    private int getStatusCode(final ClientResponse<?> response) {
        final Status status = response.getResponseStatus();
        return status == null ? UNKNOWN_STATUS_CODE : status.getStatusCode();
    }


    private String getMessage(final ContentProvider contentProvider, final ClientResponse<?> response) {
        response.resetStream();
        final ILegacyContentProviderErrorResponseBodyReader reader = LegacyContentProviderErrorResponseBodyReaderFactory.create(contentProvider);
        final String message = reader.read(response);
        if (message == null)
            return DEFAULT_MESSAGE;
        else
            return message;
    }
}
