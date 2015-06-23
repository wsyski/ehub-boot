package com.axiell.ehub.provider.generic;

import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.security.HmacSha1Function;
import com.axiell.ehub.util.StringConverter;

import java.util.Date;

public class Signature {
    private static final String SIGNATURE_FORMAT = "%s\n%s\n%d\n%s\n%d";

    private long time = new Date().getTime() / 1000;
    private ContentProviderName contentProviderName;
    private long ehubConsumerId;
    private String siteId;
    private String patronId;
    private byte[] secretKey;

    public Signature(final ContentProviderName contentProviderName, final String siteId, final long ehubConsumerId, final String patronId, final String secretKey) {
        this.contentProviderName = contentProviderName;
        this.siteId = siteId;
        this.ehubConsumerId = ehubConsumerId;
        this.patronId = patronId;
        this.secretKey = StringConverter.base64Decode(secretKey);
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        byte[] input = StringConverter.getBytesInUtf8(String.format(SIGNATURE_FORMAT, contentProviderName.name(), siteId, ehubConsumerId, patronId, time));
        return StringConverter.base64Encode(HmacSha1Function.hmacSha1(input, secretKey));
    }
}
