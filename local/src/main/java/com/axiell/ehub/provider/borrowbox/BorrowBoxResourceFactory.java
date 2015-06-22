package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

class BorrowBoxResourceFactory {

    private BorrowBoxResourceFactory() {
    }

    public static IBorrowBoxResource create(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(API_BASE_URL);
        ResteasyClient client = new ResteasyClientBuilder().build();
        client.register(new BorrowBoxClientRequestFilter(contentProviderConsumer, patron, language));
        ResteasyWebTarget target = client.target(baseUrl);
        return target.proxy(IBorrowBoxResource.class);
    }
}
