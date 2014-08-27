package com.axiell.ehub.support.request;

import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

class SupportClientExecutor extends ApacheHttpClient4Executor {
    private final SupportRequest supportRequest;

    SupportClientExecutor(final SupportRequest supportRequest) {
        this.supportRequest = supportRequest;
    }

    SupportClientExecutor(final DefaultHttpClient sslHttpClient, final SupportRequest supportRequest) {
        super(sslHttpClient);
        this.supportRequest = supportRequest;
    }

    @Override
    public final ClientResponse execute(final ClientRequest clientRequest) throws Exception {
        final String httpMethod = clientRequest.getHttpMethod();
        final String uri = clientRequest.getUri();
        supportRequest.setHttpMethod(httpMethod);
        supportRequest.setUri(uri);
        final ClientResponse response = customExecute(clientRequest);
        if (response == null)
            return super.execute(clientRequest);
        return response;
    }

    protected ClientResponse customExecute(final ClientRequest clientRequest) throws Exception {
        return null;
    }
}
