package com.axiell.ehub.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging post process interceptor
 */
@Provider
@ServerInterceptor
public final class LoggingPostProcessInterceptor implements PostProcessInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPostProcessInterceptor.class);

    @Context
    HttpServletRequest servletRequest;

    @Override
    public void postProcess(final ServerResponse serverResponse) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ToString.getLf() + ToString.fromServerResponse(serverResponse));
        }
    }

}
