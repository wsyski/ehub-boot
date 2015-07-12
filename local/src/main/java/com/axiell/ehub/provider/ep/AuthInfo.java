package com.axiell.ehub.provider.ep;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.security.Signature;
import com.axiell.ehub.security.UnauthorizedException;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

import static com.axiell.ehub.util.EhubUrlCodec.encode;

public class AuthInfo {
    private static final String AUTHORIZATION_HEADER_FORMAT =
            "realm=\"provider %s\" site_id=\"%s\" ehub_consumer_id=\"%s\", user_id=\"%s\" timestamp=\"%d\", signature=\"%s\"";

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
        EhubConsumer ehubConsumer = contentProviderConsumer.getEhubConsumer();
        long ehubConsumerId = ehubConsumer.getId();
        String encodedContentProviderName = encode(contentProvider.getName());
        String encodedSiteId =  encode(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID));
        String secretKey = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY);
        String encodedUserId =  encode(getUserId());
        Signature signature = new Signature(getSignatureItems(encodedContentProviderName, encodedSiteId, ehubConsumerId, encodedUserId, timestamp), secretKey);
        return String.format(AUTHORIZATION_HEADER_FORMAT, encodedContentProviderName, encodedSiteId, ehubConsumerId, encodedUserId, timestamp, signature.toString());
    }

    private String getUserId() {
        String userIdValueAsString = contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_USER_ID_VALUE);
        EpUserIdValue epUserIdValue;
        try {
            epUserIdValue = EpUserIdValue.valueOf(userIdValueAsString);
        } catch (IllegalArgumentException ex) {
            throw new NotImplementedException("User Id can not be calculated for EP_USER_ID_VALUE: '" + userIdValueAsString + "'");
        }
        String userId;
        switch (epUserIdValue) {
            case LIBRARY_CARD:
                if (patron.hasLibraryCard()) {
                    userId = patron.getLibraryCard();
                } else {
                    throw new UnauthorizedException(ErrorCause.MISSING_LIBRARY_CARD);
                }
                break;
            case PATRON_ID:
                if (patron.hasId()) {
                    userId = patron.getId();
                } else {
                    throw new UnauthorizedException(ErrorCause.MISSING_LIBRARY_CARD);
                }
                break;
            default:
                throw new NotImplementedException("User Id can not be calculated for EP_USER_ID_VALUE: '" + epUserIdValue + "'");
        }
        return userId;
    }

    private static List<?> getSignatureItems(final String contentProviderName, final String siteId, final long ehubConsumerId, final String userId,
                                             final long timestamp) {
        return Lists.newArrayList(contentProviderName, siteId, ehubConsumerId, userId, timestamp);
    }
}
