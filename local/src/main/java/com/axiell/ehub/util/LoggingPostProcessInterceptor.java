package com.axiell.ehub.util;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * Logging post process interceptor
 */
@Provider
@ServerInterceptor
public final class LoggingPostProcessInterceptor implements PostProcessInterceptor {
    private static final Logger LOGGER = Logger.getLogger(LoggingPostProcessInterceptor.class);

    @Context
    HttpServletRequest servletRequest;

    @Override
    public void postProcess(final ServerResponse serverResponse) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ToString.getLf() + ToString.fromServerResponse(serverResponse));
        }
    }

}
