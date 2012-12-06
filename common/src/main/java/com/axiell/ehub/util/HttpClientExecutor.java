package com.axiell.ehub.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

/**
 * HTTP Client Executor.
 */
public class HttpClientExecutor extends ApacheHttpClient4Executor {

    /**
     * Constructs a new {@link HttpClientExecutor}.
     */
    public HttpClientExecutor() {
        super(createHttpClient());
    }

    /**
     * @see org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor#execute(org.jboss.resteasy.client.ClientRequest)
     */
    @Override
    public ClientResponse<?> execute(final ClientRequest request) throws Exception {
        ClientResponse clientResponse = super.execute(request);
        return clientResponse;
    }

    /**
     * Creates a new {@link HttpClient}.
     *
     * @return a new {@link HttpClient}
     */
    private static HttpClient createHttpClient() {
        ClientConnectionManager safeClientConnManager = new ThreadSafeClientConnManager();
        HttpClient httpClient = new DefaultHttpClient(safeClientConnManager);
        return httpClient;
    }
}
