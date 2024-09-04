package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

abstract class AbstractAccessTokenResourceFactory {

    protected abstract ContentProviderPropertyKey getOAuthUrlPropertyKey();

    IAccessTokenResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final ContentProviderPropertyKey oauthUrlPropertyKey = getOAuthUrlPropertyKey();
        final String oauthUrl = contentProvider.getProperty(oauthUrlPropertyKey);
        ResteasyClient client = new ResteasyClientBuilderImpl().build();
        ResteasyWebTarget target = client.target(oauthUrl);
        return target.proxy(IAccessTokenResource.class);
    }
}
