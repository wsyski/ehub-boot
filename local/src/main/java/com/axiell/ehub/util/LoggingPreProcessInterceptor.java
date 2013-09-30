package com.axiell.ehub.util;

import com.axiell.ehub.util.strings.ToString;
import org.apache.commons.lang3.time.StopWatch;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPreProcessInterceptor.class);

    @Context
    HttpServletRequest servletRequest;

    @Override
    public ServerResponse preProcess(HttpRequest httpRequest, ResourceMethod method) throws Failure, WebApplicationException {
	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	httpRequest.setAttribute("stopWatch", stopWatch);
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("Request:" + ToString.lineFeed() + ToString.httpRequestToString(httpRequest));
	}
	return null;
    }
}
