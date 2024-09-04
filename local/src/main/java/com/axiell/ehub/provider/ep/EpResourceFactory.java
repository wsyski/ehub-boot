package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.ContentProvider;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.springframework.beans.factory.annotation.Required;

public class EpResourceFactory {

    private ClientHttpEngine httpEngine;

    public <E> E create(final Class<E> clazz, final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL);
        ResteasyClient client = new ResteasyClientBuilderImpl().httpEngine(httpEngine).build();
        client.register(new EpClientRequestFilter(contentProviderConsumer, patron));
        ResteasyWebTarget target = client.target(baseUrl);
        return target.proxy(clazz);
    }

    @Required
    public void setHttpEngine(final ClientHttpEngine httpEngine) {
        this.httpEngine = httpEngine;
    }
}
