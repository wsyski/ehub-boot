package com.axiell.ehub.provider.sdk;

import com.axiell.ehub.provider.ContentProviderName;

public class Authorization {
    private static final String AUTHORIZATION_HEADER_FORMAT = "Provider Realm=%s ConsumerId=%s, PatronId=%s SignatureDate=%d, Signature=%s";

    private ContentProviderName contentProviderName;
    private long ehubConsumerId;
    private String patronId;
    private String secretKey;

    public Authorization(final ContentProviderName contentProviderName, final long ehubConsumerId, final String patronId, final String secretKey) {
        this.contentProviderName = contentProviderName;
        this.ehubConsumerId = ehubConsumerId;
        this.patronId = patronId;
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        Signature signature = new Signature(contentProviderName, ehubConsumerId, patronId, secretKey);
        return String.format(AUTHORIZATION_HEADER_FORMAT, contentProviderName, ehubConsumerId, patronId, signature.getTime(), signature.toString());
    }
}
