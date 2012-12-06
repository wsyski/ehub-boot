package com.axiell.ehub.util;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * Logging post process interceptor
 */
@Provider
@ServerInterceptor
public final class LoggingPreProcessInterceptor implements PreProcessInterceptor {
    private static final Logger LOGGER = Logger.getLogger(LoggingPreProcessInterceptor.class);

    @Context
    HttpServletRequest servletRequest;

    @Override
    public ServerResponse preProcess(HttpRequest httpRequest, ResourceMethod method) throws Failure, WebApplicationException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ToString.getLf() + ToString.fromHttpRequest(httpRequest));
        }
        return null;
    }
}
