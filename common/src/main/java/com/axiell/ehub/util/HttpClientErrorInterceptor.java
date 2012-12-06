package com.axiell.ehub.util;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;

/**
 * Client Exception Interceptor.
 */
public class HttpClientErrorInterceptor implements ClientErrorInterceptor {
    private static final Logger LOGGER = Logger.getLogger(HttpClientErrorInterceptor.class);

    /**
     * @see org.jboss.resteasy.client.core.ClientErrorInterceptor#handle(org.jboss.resteasy.client.ClientResponse)
     */
    @Override
    public void handle(final ClientResponse<?> response) {
    }
}
