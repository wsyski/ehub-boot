package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.cache.CacheFactory;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

final class ElibResourceFactory {

    private ElibResourceFactory() {
    }

    public static IElibResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(API_BASE_URL);
        return ProxyFactory.create(IElibResource.class, baseUrl);
    }
}
