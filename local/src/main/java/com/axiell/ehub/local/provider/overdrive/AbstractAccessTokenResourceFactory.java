package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.controller.provider.json.JsonProvider;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
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
        final JacksonJsonProvider jacksonJsonProvider = new JsonProvider();
        return JAXRSClientFactory.create(baseUrl, IAccessTokenResource.class, Collections.singletonList(jacksonJsonProvider), Collections.singletonList(loggingFeature), null);
    }
}
