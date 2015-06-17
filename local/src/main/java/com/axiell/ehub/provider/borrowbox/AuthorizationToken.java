package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.HmacSha256Function;

import java.util.Date;

public class AuthorizationToken {
    private long time=new Date().getTime()/1000;

    public AuthorizationToken(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        siteId=contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SITE_ID);
        libraryId=contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_LIBRARY_ID);
        secretKey=contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SECRET_KEY);
        libraryCard=patron.getLibraryCard();
    }

    private String siteId;
    private String libraryId;

    private String libraryCard;

    private String secretKey;

    public String getLibraryId() {
        return libraryId;
    }

    public String getLibraryCard() {
        return libraryCard;
    }

    private String getMessage() {
        return String.format("%s\n%d\n%s", libraryId, time, libraryCard);
    }

    @Override
    public String toString() {
        return String.format("Credential=%s, SignatureDate=%d, Signature=%s", siteId, time, HmacSha256Function.hash(secretKey, getMessage()));
    }
}
