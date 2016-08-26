package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.beans.factory.annotation.Required;

public class BorrowBoxResourceFactory {
    private ClientHttpEngine httpEngine;

    public IBorrowBoxResource create(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL);
        ResteasyClient client = new ResteasyClientBuilder().httpEngine(httpEngine).build();
        client.register(new BorrowBoxClientRequestFilter(contentProviderConsumer, patron, language));
        ResteasyWebTarget target = client.target(baseUrl);
        return target.proxy(IBorrowBoxResource.class);
    }

    @Required
    public void setHttpEngine(final ClientHttpEngine httpEngine) {
        this.httpEngine = httpEngine;
    }
}
