package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

class AvailabilityResourceFactory {

    private AvailabilityResourceFactory() {
    }

    static IAvailabilityResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(API_BASE_URL);
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(baseUrl);
        return target.proxy(IAvailabilityResource.class);
    }
}
