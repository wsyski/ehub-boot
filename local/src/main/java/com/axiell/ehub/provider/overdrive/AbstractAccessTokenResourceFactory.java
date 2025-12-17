package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

abstract class AbstractAccessTokenResourceFactory {

    protected abstract ContentProviderPropertyKey getOAuthUrlPropertyKey();

    IAccessTokenResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final ContentProviderPropertyKey oauthUrlPropertyKey = getOAuthUrlPropertyKey();
        final String oauthUrl = contentProvider.getProperty(oauthUrlPropertyKey);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(oauthUrl);
        return WebResourceFactory.newResource(IAccessTokenResource.class, target);

    }
}
