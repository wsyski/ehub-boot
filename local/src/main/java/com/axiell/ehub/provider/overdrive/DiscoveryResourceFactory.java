package com.axiell.ehub.provider.overdrive;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

import org.jboss.resteasy.client.ProxyFactory;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;

final class DiscoveryResourceFactory {
    
    private DiscoveryResourceFactory() {	
    }

    static IDiscoveryResource create(final ContentProviderConsumer contentProviderConsumer) {
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final String baseUrl = contentProvider.getProperty(API_BASE_URL);
	return ProxyFactory.create(IDiscoveryResource.class, baseUrl);
    }
}
