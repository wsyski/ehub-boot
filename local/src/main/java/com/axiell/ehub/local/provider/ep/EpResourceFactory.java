package com.axiell.ehub.local.provider.ep;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.controller.provider.json.JsonProvider;
import com.axiell.ehub.common.provider.ContentProvider;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class EpResourceFactory {

    public <E> E create(final Class<E> clazz, final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL);
        final EpClientRequestFilter epClientRequestFilter = new EpClientRequestFilter(contentProviderConsumer, patron);
        final LoggingFeature loggingFeature = new LoggingFeature();
        final JacksonJsonProvider jacksonJsonProvider = new JsonProvider();
        return JAXRSClientFactory.create(baseUrl, clazz, Arrays.asList(jacksonJsonProvider, epClientRequestFilter), Collections.singletonList(loggingFeature), null);
    }
}
