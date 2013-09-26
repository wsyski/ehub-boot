/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import javax.ws.rs.core.Response.Status;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;

/**
 * This Aspect converts {@link ClientResponseFailure}s thrown by the {@link IContentProviderDataAccessor}s to
 * {@link InternalServerErrorException}s.
 */
@Aspect
public class ContentProviderResponseFailureAspect {
    private static final int UNKNOWN_STATUS_CODE = -1;
    private static final String UNKNOWN_CONTENT_PROVIDER = "unknown";    
    private static final String DEFAULT_MESSAGE = "An unepected exception occurred while trying to connect to the Content Provider";

    /**
     * Converts the {@link ClientResponseFailure} to an {@link InternalServerErrorException}.
     * 
     * @param joinPoint the {@link JoinPoint} to use
     * @param crf the {@link ClientResponseFailure} to convert
     * @throws InternalServerErrorException when this method is invoked
     */
    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.provider.IContentProviderDataAccessor.*(..))", throwing = "crf")
    public void toInternalServerErrorException(final JoinPoint joinPoint, final ClientResponseFailure crf) {
        final ClientResponse<?> response = crf.getResponse();        
        final String message = getMessage(response);
        final ErrorCauseArgument nameArg = makeContentProviderNameErrorCauseArgument(joinPoint);
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
    
    /**
     * Returns an {@link ErrorCauseArgument} containing the name of the {@link ContentProvider} that caused the
     * {@link ClientResponseFailure}.
     * 
     * @param joinPoint the {@link JoinPoint} to use
     * @return an {@link ErrorCauseArgument}
     */
    private ErrorCauseArgument makeContentProviderNameErrorCauseArgument(final JoinPoint joinPoint) {	
	Object contentProviderName = getContentProviderName(joinPoint);	
        return new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, contentProviderName);
    }
    
    private Object getContentProviderName(JoinPoint joinPoint) {
	ContentProvider contentProvider = getContentProvider(joinPoint);
	return contentProvider == null ? UNKNOWN_CONTENT_PROVIDER : contentProvider.getName();	
    }
    
    private ContentProvider getContentProvider(JoinPoint joinPoint) {
	ContentProviderConsumer contentProviderConsumer = getContentProviderConsumer(joinPoint);
	return contentProviderConsumer == null ? null : contentProviderConsumer.getContentProvider();
    }
    
    private ContentProviderConsumer getContentProviderConsumer(JoinPoint joinPoint) {
	for (Object arg : joinPoint.getArgs()) {
	    if (arg instanceof ContentProviderConsumer)
		return (ContentProviderConsumer) arg;
	}
	return null;
    }

    /**
     * Gets the error message from the {@link ClientResponse}. If the {@link ClientResponse} doesn't contain a message
     * the default message is returned instead.
     * 
     * @param response the {@link ClientResponse} to get the message from
     * @return an error message
     */
    private String getMessage(final ClientResponse<?> response) {
        final String message = response.getEntity(String.class);

        if (message == null) {
            return DEFAULT_MESSAGE;
        } else {
            return message;
        }
    }
}
