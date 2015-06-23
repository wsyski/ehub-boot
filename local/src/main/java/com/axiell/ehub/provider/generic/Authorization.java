package com.axiell.ehub.provider.generic;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;

public class Authorization {
    private static final String AUTHORIZATION_HEADER_FORMAT = "Provider Realm=%s SiteId=%s ConsumerId=%s, PatronId=%s SignatureDate=%d, Signature=%s";

    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;

    public Authorization(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
    }

    @Override
    public String toString() {
        ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        ContentProviderName contentProviderName = contentProvider.getName();
        EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        long ehubConsumerId = ehubConsumer.getId();
        String siteId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.GENERIC_SITE_ID);
        String secretKey = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.GENERIC_SECRET_KEY);
        String patronId = patron.getId();
        Signature signature = new Signature(contentProviderName, siteId, ehubConsumerId, patronId, secretKey);
        return String.format(AUTHORIZATION_HEADER_FORMAT, contentProviderName, siteId, ehubConsumerId, patronId, signature.getTime(), signature.toString());
    }
}
