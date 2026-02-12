package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.controller.provider.json.JsonProvider;
import com.axiell.ehub.common.provider.ContentProvider;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import java.util.Collections;

import static com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey.PATRON_API_BASE_URL;

final class CirculationResourceFactory {

    private CirculationResourceFactory() {
    }

    static ICirculationResource create(final ContentProviderConsumer contentProviderConsumer) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(PATRON_API_BASE_URL);
        final LoggingFeature loggingFeature = new LoggingFeature();
        final JacksonJsonProvider jacksonJsonProvider = new JsonProvider();
        return JAXRSClientFactory.create(baseUrl, ICirculationResource.class, Collections.singletonList(jacksonJsonProvider), Collections.singletonList(loggingFeature), null);
    }
}
