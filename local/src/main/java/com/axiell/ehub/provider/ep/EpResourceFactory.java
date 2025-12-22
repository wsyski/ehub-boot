package com.axiell.ehub.provider.ep;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.provider.ContentProvider;
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
        final JsonProvider jsonProvider = new JsonProvider();
        return JAXRSClientFactory.create(baseUrl, clazz, Arrays.asList(jsonProvider, epClientRequestFilter), Collections.singletonList(loggingFeature),null);
    }
}
