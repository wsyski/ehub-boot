package com.axiell.ehub.logging;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;

@Provider
public class ClientLoggingInterceptor implements ClientRequestFilter, ClientResponseFilter, ReaderInterceptor {
    private static final Logger TIME_LOGGER = LoggerFactory.getLogger("time");
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientLoggingInterceptor.class);
    private static final String SEPARATOR = "; ";
    private static final String STOP_WATCH_PROPERTY = "stopWatch";

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        requestContext.setProperty(STOP_WATCH_PROPERTY, stopWatch);
    }

    @Override
    public void filter(final ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        final StopWatch stopWatch = (StopWatch) requestContext.getProperty(STOP_WATCH_PROPERTY);
        stopWatch.stop();
        String timeMessage = getTimeMessage(requestContext, responseContext, stopWatch.getTime());
        TIME_LOGGER.info(timeMessage);
        if (LOGGER.isDebugEnabled()) {
            String headersMessage = getHeadersMessage(responseContext.getHeaders());
            LOGGER.debug(timeMessage);
            LOGGER.debug(headersMessage);
        }
    }

    @Override
    public Object aroundReadFrom(final ReaderInterceptorContext context) throws IOException {
        Object entity = context.proceed();
        if (LOGGER.isDebugEnabled()) {
            String entityMessage = getEntityMessage(entity);
            LOGGER.debug(entityMessage);
        }
        return entity;
    }

    private String getEntityMessage(final Object entity) {
        return "entity: " + ToString.toString(entity);
    }

    private String getHeadersMessage(final MultivaluedMap<String, String> headers) {
        return "headers: " + ToString.toString(headers);
    }

    private String getTimeMessage(final ClientRequestContext requestContext, final ClientResponseContext responseContext, final long elapsedTime) {
        return requestContext.getMethod() + SEPARATOR + requestContext.getUri() + SEPARATOR + elapsedTime + SEPARATOR + responseContext.getStatus();
    }


}

