package com.axiell.ehub.util;

import org.jboss.resteasy.annotations.interception.ClientInterceptor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;

/**
 * Logging Client Interceptor
 */
@Provider
@ClientInterceptor
public class LoggingClientExecutionInterceptor implements ClientExecutionInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingClientExecutionInterceptor.class);


    @Override
    public ClientResponse<?> execute(ClientExecutionContext ctx) throws Exception {
        ClientRequest clientRequest = ctx.getRequest();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ToString.getLf() + ToString.fromClientRequest(clientRequest));
        }
        ClientResponse<?> clientResponse = ctx.proceed();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ToString.getLf() + ToString.fromClientResponse(clientResponse));
        }
        return clientResponse;
    }
}
