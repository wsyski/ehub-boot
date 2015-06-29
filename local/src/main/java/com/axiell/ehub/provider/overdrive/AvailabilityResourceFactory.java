package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.ProxyFactory;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

final class AvailabilityResourceFactory {

    private AvailabilityResourceFactory() {
    }

    static IAvailabilityResource create(final ContentProviderConsumer contentProviderConsumer) {
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final String baseUrl = contentProvider.getProperty(API_BASE_URL);
	return ProxyFactory.create(IAvailabilityResource.class, baseUrl);
    }
}
