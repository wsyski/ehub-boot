package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.HmacSha256Function;
import com.axiell.ehub.util.StringConverter;

import java.util.Date;

public class Signature {
    private static final String SIGNATURE_FORMAT = "%s\n%s\n%d";

    private String siteId;
    private String libraryCard;
    private byte[] secretKey;
    private long timestamp;

    public Signature(final String siteId, final String libraryCard, final String secretKey, final long timestamp) {
        this.siteId = siteId;
        this.libraryCard = libraryCard;
        this.secretKey = StringConverter.base64Decode(secretKey);
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        byte[] input = StringConverter.getBytesInUtf8(String.format(SIGNATURE_FORMAT, siteId, libraryCard, timestamp));
        return StringConverter.base64Encode(HmacSha256Function.hmacSha256(input, secretKey));
    }
}
