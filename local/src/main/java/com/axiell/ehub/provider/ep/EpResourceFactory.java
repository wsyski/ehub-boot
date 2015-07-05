package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

class EpResourceFactory {

    private EpResourceFactory() {
    }

    public static IEpResource create(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(API_BASE_URL);
        ResteasyClient client = new ResteasyClientBuilder().build();
        client.register(new EpClientRequestFilter(contentProviderConsumer, patron));
        ResteasyWebTarget target = client.target(baseUrl);
        return target.proxy(IEpResource.class);
    }
}
