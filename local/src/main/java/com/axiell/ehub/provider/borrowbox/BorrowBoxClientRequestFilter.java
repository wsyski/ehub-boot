package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.auth.Patron;
import com.axiell.ehub.util.PatronUtil;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class BorrowBoxClientRequestFilter implements ClientRequestFilter {

    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;
    private String language;

    public BorrowBoxClientRequestFilter(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
        this.language = language;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        String libraryCard= PatronUtil.getMandatoryLibraryCard(patron);
        headers.add(HttpHeaders.AUTHORIZATION, new AuthInfo(contentProviderConsumer, patron));
        headers.add("Accept-Language", language);
        headers.add("X-BorrowBox-Site", contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SITE_ID));
        headers.add("X-BorrowBox-User", libraryCard);
    }
}
