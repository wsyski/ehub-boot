package com.axiell.ehub.provider.overdrive;

import org.jboss.resteasy.client.ProxyFactory;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

abstract class AbstractAccessTokenResourceFactory {

    protected abstract ContentProviderPropertyKey getOAuthUrlPropertyKey();
    
    IAccessTokenResource create(final ContentProviderConsumer contentProviderConsumer) {
	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final ContentProviderPropertyKey oauthUrlPropertyKey = getOAuthUrlPropertyKey();
	final String oauthUrl = contentProvider.getProperty(oauthUrlPropertyKey);
	return ProxyFactory.create(IAccessTokenResource.class, oauthUrl);
    }
}
