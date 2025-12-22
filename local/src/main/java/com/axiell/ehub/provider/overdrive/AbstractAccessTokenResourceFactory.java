package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import java.util.Collections;

abstract class AbstractAccessTokenResourceFactory {

    protected abstract ContentProviderPropertyKey getOAuthUrlPropertyKey();

    IAccessTokenResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final ContentProviderPropertyKey oauthUrlPropertyKey = getOAuthUrlPropertyKey();
        final String baseUrl = contentProvider.getProperty(oauthUrlPropertyKey);
        final LoggingFeature loggingFeature = new LoggingFeature();
        final JsonProvider jsonProvider = new JsonProvider();
        return JAXRSClientFactory.create(baseUrl, IAccessTokenResource.class, Collections.singletonList(jsonProvider), Collections.singletonList(loggingFeature), null);
    }
}
