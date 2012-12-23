package com.axiell.ehub.util;

import org.jboss.resteasy.annotations.interception.ClientInterceptor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;

/**
 * Client Exception Interceptor.
 */
@Provider
@ClientInterceptor
public class LoggingClientErrorInterceptor implements ClientErrorInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingClientErrorInterceptor.class);

    /**
     * @see org.jboss.resteasy.client.core.ClientErrorInterceptor#handle(org.jboss.resteasy.client.ClientResponse)
     */
    @Override
    public void handle(final ClientResponse<?> clientResponse) {
        LOGGER.error(" Response:" + ToString.getLf() + ToString.fromClientResponse(clientResponse));
    }
}
