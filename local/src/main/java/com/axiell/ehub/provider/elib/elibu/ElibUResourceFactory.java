package com.axiell.ehub.provider.elib.elibu;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

final class ElibUResourceFactory {

    private ElibUResourceFactory() {
    }

    public static IElibUResource create(final String baseUrl) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(baseUrl);
        return WebResourceFactory.newResource(IElibUResource.class, target);

    }
}
