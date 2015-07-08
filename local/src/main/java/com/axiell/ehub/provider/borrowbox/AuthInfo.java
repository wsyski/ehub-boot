package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;

import java.util.Date;

public class AuthInfo {
    private static final String AUTHORIZATION_HEADER_FORMAT = "Credential=%s, SignatureDate=%d, Signature=%s";

    private long timestamp = new Date().getTime() / 1000;
    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;

    public AuthInfo(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
    }

    @Override
    public String toString() {
        String siteId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SITE_ID);
        String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_LIBRARY_ID);
        String secretKey = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SECRET_KEY);
        String libraryCard = patron.getLibraryCard();
        Signature signature = new Signature(siteId, libraryCard, secretKey, timestamp);
        return String.format(AUTHORIZATION_HEADER_FORMAT, libraryId, timestamp, signature.toString());
    }

    void setTimestamp(final long timestamp) {
        this.timestamp=timestamp;
    }
}
