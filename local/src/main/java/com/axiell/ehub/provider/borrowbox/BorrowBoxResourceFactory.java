package com.axiell.ehub.provider.borrowbox;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProvider;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class BorrowBoxResourceFactory {

    public IBorrowBoxResource create(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String baseUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.API_BASE_URL);


        Client client = ClientBuilder.newClient();
        client.register(new BorrowBoxClientRequestFilter(contentProviderConsumer, patron, language));
        WebTarget target = client.target(baseUrl);
        return WebResourceFactory.newResource(IBorrowBoxResource.class, target);
    }
}
