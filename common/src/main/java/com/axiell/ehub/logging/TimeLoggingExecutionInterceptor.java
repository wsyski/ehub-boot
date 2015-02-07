package com.axiell.ehub.logging;

import org.apache.commons.lang3.time.StopWatch;
import org.jboss.resteasy.annotations.interception.ClientInterceptor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.Provider;

@Provider
@ClientInterceptor
public class TimeLoggingExecutionInterceptor implements ClientExecutionInterceptor {
    private static final String INVOCATION_TRAIL_NOT_AVAILABLE = "INVOCATION_TRAIL_NOT_AVAILABLE";
    private static final String SEPARATOR = "; ";
    private static final Logger TIME_LOGGER = LoggerFactory.getLogger("time");

    @Override
    public ClientResponse execute(final ClientExecutionContext ctx) throws Exception {
        final StopWatch stopWatch = new StopWatch();
        Exception exception = null;
        ClientResponse response = null;

        stopWatch.start();
        try {
            response = ctx.proceed();
        } catch (Exception e) {
            exception = e;
        }
        stopWatch.stop();
        logTime(ctx, stopWatch, exception, response);
        throwExceptionIfPresent(exception);
        return response;
    }

    private void throwExceptionIfPresent(final Exception exception) throws Exception {
        if (exception != null) {
            throw exception;
        }
    }

    private void logTime(final ClientExecutionContext ctx, final StopWatch stopWatch, final Exception exception, final ClientResponse response) throws
            Exception {
        TIME_LOGGER.info(createLogMessage(ctx, stopWatch.getTime(), response, exception));
    }

    private String createLogMessage(final ClientExecutionContext executionContext, final long elapsedTime, final ClientResponse httpResponse, final Exception exception) throws
            Exception {
        return INVOCATION_TRAIL_NOT_AVAILABLE + SEPARATOR + executionContext.getRequest().getHttpMethod() + SEPARATOR + executionContext.getRequest().getUri() + SEPARATOR + elapsedTime + SEPARATOR
                + (httpResponse != null ? httpResponse.getStatus() : (exception == null ? "" : exception.getClass().getName() + ": " + exception.getMessage()));
    }


}

