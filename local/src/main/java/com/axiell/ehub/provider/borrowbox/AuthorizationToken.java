package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.HmacSha256Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AuthorizationToken {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationToken.class);

    private long time = new Date().getTime() / 1000;
    //private long time = 1434616780;
    private ContentProviderConsumer contentProviderConsumer;
    private Patron patron;


    public AuthorizationToken(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        this.contentProviderConsumer = contentProviderConsumer;
        this.patron = patron;
    }

    @Override
    public String toString() {
        String siteId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SITE_ID);
        String libraryId = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_LIBRARY_ID);
        String secretKey = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SECRET_KEY);
        String libraryCard = patron.getLibraryCard();
        String data = String.format("%s\n%s\n%d", siteId, libraryCard, time);
        //LOGGER.debug(" secretKey: " + secretKey+" data: " + data);
        return String.format("Credential=%s, SignatureDate=%d, Signature=%s", libraryId, time, HmacSha256Function.hash(secretKey, data));
    }
}
