package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class ZinioResourceFactory {

    private ZinioResourceFactory() {
    }

    public static IZinioResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(baseUrl);
        return WebResourceFactory.newResource(IZinioResource.class, target);
    }
}
