package com.axiell.ehub.provider.generic;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.security.Signature;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

public class AuthInfo {
    private static final String AUTHORIZATION_HEADER_FORMAT =
            "provider realm=\"%s\" site_id=\"%s\" ehub_consumer_id=\"%s\", ehub_patron_id=\"%s\" timestamp=\"%d\", signature=\"%s\"";

    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;
    private long timestamp = new Date().getTime() / 1000;

    public AuthInfo(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
    }

    @Override
    public String toString() {
        ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        String contentProviderName = contentProvider.getName().name();
        EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        long ehubConsumerId = ehubConsumer.getId();
        String siteId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.GENERIC_SITE_ID);
        String secretKey = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.GENERIC_SECRET_KEY);
        String patronId = patron.getId();
        Signature signature = new Signature(getSignatureItems(contentProviderName, siteId, ehubConsumerId, patronId, timestamp), secretKey);
        return String.format(AUTHORIZATION_HEADER_FORMAT, contentProviderName, siteId, ehubConsumerId, patronId, timestamp, signature.toString());
    }

    public static List<?> getSignatureItems(final String contentProviderName, final String siteId, final long ehubConsumerId, final String patronId,
                                            final long timestamp) {
        return Lists.newArrayList(contentProviderName, siteId, ehubConsumerId, patronId, timestamp);
    }
}
