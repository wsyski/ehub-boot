package com.axiell.ehub.logging;

import org.apache.commons.lang3.time.StopWatch;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * Logging post process interceptor
 */
@Provider
@ServerInterceptor
public final class LoggingPostProcessInterceptor implements PostProcessInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPostProcessInterceptor.class);

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void postProcess(final ServerResponse serverResponse) {
        if (LOGGER.isDebugEnabled()) {
            StopWatch stopWatch = (StopWatch) servletRequest.getAttribute("stopWatch");
            LOGGER.debug("Response time: " + stopWatch.getTime() + ToStringConverter.lineFeed() + ToStringConverter.serverResponseToString(serverResponse));
        }
    }

}
