package com.axiell.ehub.logging;

import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.time.StopWatch;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging post process interceptor
 */
@Provider
@ServerInterceptor
public final class LoggingPreProcessInterceptor implements PreProcessInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPreProcessInterceptor.class);

    @Override
    public ServerResponse preProcess(HttpRequest httpRequest, final ResourceMethodInvoker resourceMethodInvoker) throws Failure {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        httpRequest.setAttribute("stopWatch", stopWatch);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request:" + ToStringConverter.lineFeed() + ToStringConverter.httpRequestToString(httpRequest));
        }
        return null;
    }
}
