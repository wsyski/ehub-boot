package com.axiell.ehub.logging;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class TimeLoggingClientFilter implements ClientRequestFilter, ClientResponseFilter {
    private static final Logger TIME_LOGGER = LoggerFactory.getLogger("time");
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
    }

    private String getTimeMessage(final ClientRequestContext requestContext, final ClientResponseContext responseContext, final long elapsedTime) {
        return requestContext.getMethod() + SEPARATOR + requestContext.getUri() + SEPARATOR + elapsedTime + SEPARATOR + responseContext.getStatus();
    }
}

