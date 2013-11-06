package com.axiell.ehub.provider.overdrive;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.PATRON_API_BASE_URL;

import org.jboss.resteasy.client.ProxyFactory;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;

final class CirculationResourceFactory {

    private CirculationResourceFactory() {	
    }
    
    static ICirculationResource create(final ContentProviderConsumer contentProviderConsumer) {
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final String baseUrl = contentProvider.getProperty(PATRON_API_BASE_URL);
	return ProxyFactory.create(ICirculationResource.class, baseUrl);
    }
}
