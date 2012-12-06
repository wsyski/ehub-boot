package com.axiell.ehub.util;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.interception.ClientInterceptor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;

import javax.ws.rs.ext.Provider;

/**
 * Logging Client Interceptor
 */
@Provider
@ClientInterceptor
public class LoggingClientExecutionIterceptor implements ClientExecutionInterceptor {
    private static final Logger LOGGER = Logger.getLogger(LoggingClientExecutionIterceptor.class);


    @Override
    public ClientResponse execute(ClientExecutionContext ctx) throws Exception {
        ClientRequest clientRequest = ctx.getRequest();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ToString.getLf() + ToString.fromClientRequest(clientRequest));
        }
        ClientResponse clientResponse = ctx.proceed();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ToString.getLf() + ToString.fromClientResponse(clientResponse));
        }
        return clientResponse;
    }
}
