/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.exception.ResteasyClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.util.XjcSupport;

/**
 * This Aspect converts {@link ClientResponseFailure}s thrown by the
 * {@link EhubClient} to {@link EhubException}s and {@link EhubRuntimeException}
 * s thrown by the {@link EhubClient} to {@link EhubException}s.
 */
@Aspect
public class ClientResponseFailureAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientResponseFailureAspect.class);

    /**
     * Converts the {@link ClientResponseFailure} to an {@link EhubException}.
     *
     * @param crf the {@link ClientResponseFailure} to convert
     * @throws EhubException when this method is invoked
     */
    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.EhubClient.*(..))", throwing = "crf")
    public void toEhubException(final JoinPoint joinPoint, final ClientResponseFailure crf) throws EhubException {
        final EhubError ehubError = makeEhubError(crf);
        throw new EhubException(ehubError);
    }

    private EhubError makeEhubError(final ClientResponseFailure crf) {
        final ClientResponse<?> response = crf.getResponse();
        EhubError ehubError = null;

        if (hasApplicationXmlAsContentType(response)) {
            final String xml = response.getEntity(String.class);

            if (xml == null) {
                LOGGER.warn("No xml in Ehub client response");
            } else {
                ehubError = unmarshal(xml);
            }
        }

        ehubError = ehubError == null ? ErrorCause.INTERNAL_SERVER_ERROR.toEhubError() : ehubError;
        return ehubError;
    }

    private boolean hasApplicationXmlAsContentType(ClientResponse<?> response) {
        final MultivaluedMap<String, String> headers = response.getHeaders();
        final List<String> contentTypes = headers.get(HttpHeaders.CONTENT_TYPE);

        if (contentTypes == null) {
            LOGGER.warn("No headers in Ehub client response");
            return false;
        }

        if (contentTypes.contains(MediaType.APPLICATION_XML))
            return true;

        LOGGER.warn("The content type headers does not contain '" + MediaType.APPLICATION_XML + "'");
        return false;
    }

    private EhubError unmarshal(final String xml) {
    /*
	 * Must use this "manual" unmarshaller and not
	 * 'response.getEntity(EhubError.class)' since the type of the
	 * entity in the response is String
	 */
        try {
            return XjcSupport.unmarshal(xml, EhubError.class);
        } catch (JAXBException e) {
            LOGGER.error("Could not unmarshal the xml '" + xml + "' to an 'EhubError'", e);
            return null;
        }
    }

    /**
     * Converts the {@link Throwable} to an {@link EhubException}.
     *
     * @param rce the {@link ResteasyClientException} to convert
     * @throws EhubException when this method is invoked
     */
    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.EhubClient.*(..))", throwing = "rce")
    public void toEhubException(final JoinPoint joinPoint, final ResteasyClientException rce) throws EhubException {
        LOGGER.error(rce.getMessage(), rce);
        throw new EhubException(ErrorCause.INTERNAL_SERVER_ERROR.toEhubError());
    }

    /**
     * Converts the {@link EhubRuntimeException} to an {@link EhubException}.
     *
     * @param ere the {@link EhubRuntimeException} to convert
     * @throws EhubException when this method is invoked
     */
    @AfterThrowing(pointcut = "execution(* com.axiell.ehub.EhubClient.*(..))", throwing = "ere")
    public void toEhubException(final JoinPoint joinPoint, final EhubRuntimeException ere) throws EhubException {
        final EhubError ehubError = ere.getEhubError();
        throw new EhubException(ehubError);
    }
}
