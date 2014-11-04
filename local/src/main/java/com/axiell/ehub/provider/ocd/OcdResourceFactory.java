package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.elib.library3.IElibResource;
import org.jboss.resteasy.client.ProxyFactory;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

class OcdResourceFactory {

    private OcdResourceFactory() {
    }

    public static IOcdResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(API_BASE_URL);
        return ProxyFactory.create(IOcdResource.class, baseUrl);
    }
}
