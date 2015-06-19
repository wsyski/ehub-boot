package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class BorrowBoxClientRequestFilter implements ClientRequestFilter {

    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;

    public BorrowBoxClientRequestFilter(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.add("Authorization", new AuthorizationToken(contentProviderConsumer, patron));
        headers.add("Accept-Language", "en");
        headers.add("X-BorrowBox-Site", contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SITE_ID));
        headers.add("X-BorrowBox-User", patron.getLibraryCard());
    }
}
