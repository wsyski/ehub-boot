package com.axiell.ehub.provider.generic;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class ProviderClientRequestFilter implements ClientRequestFilter {

    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;
    private String language;

    public ProviderClientRequestFilter(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
        this.language = language;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.add("Authorization", new Authorization(contentProviderConsumer, patron));
        headers.add("Accept-Language", language);
    }
}
