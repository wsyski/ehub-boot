package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.HmacSha256Function;

public class AuthenticationToken {

    public AuthenticationToken(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        libraryId=contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_LIBRARY_ID);
        secretKey=contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SECRET_KEY);
        libraryCard=patron.getLibraryCard();
    }

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
        return String.format("%s\n%s", libraryId, libraryCard);
    }

    @Override
    public String toString() {
        return String.format("Credential:%s, Signature=%s", libraryId, HmacSha256Function.hash(secretKey, getMessage()));
    }
}
