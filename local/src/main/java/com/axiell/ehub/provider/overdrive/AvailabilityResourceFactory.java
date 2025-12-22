package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.provider.ContentProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import java.util.Collections;

import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.API_BASE_URL;

class AvailabilityResourceFactory {

    private AvailabilityResourceFactory() {
    }

    static IAvailabilityResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(API_BASE_URL);
        final LoggingFeature loggingFeature = new LoggingFeature();
        final JsonProvider jsonProvider = new JsonProvider();
        return JAXRSClientFactory.create(baseUrl, IAvailabilityResource.class, Collections.singletonList(jsonProvider), Collections.singletonList(loggingFeature), null);
    }
}
