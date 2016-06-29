package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class ZinioResourceFactory {

    private ZinioResourceFactory() {
    }

    public static IZinioResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL);
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(baseUrl);
        return target.proxy(IZinioResource.class);
    }
}
