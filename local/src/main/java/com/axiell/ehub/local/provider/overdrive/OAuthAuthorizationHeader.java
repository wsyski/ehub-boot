package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_KEY;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_SECRET;

final class OAuthAuthorizationHeader {
    private static final String AUTHORIZATION_METHOD = "Basic ";
    private static final String DELIMITER = ":";
    private final String clientKey;
    private final String clientSecret;

    private OAuthAuthorizationHeader(final String clientKey, final String clientSecret) {
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
    }

    static OAuthAuthorizationHeader fromContentProviderConsumer(final ContentProviderConsumer contentProviderConsumer) {
        final String key = contentProviderConsumer.getProperty(OVERDRIVE_CLIENT_KEY);
        final String secret = contentProviderConsumer.getProperty(OVERDRIVE_CLIENT_SECRET);
        return new OAuthAuthorizationHeader(key, secret);
    }

    @Override
    public String toString() {
        final String credentials = clientKey + DELIMITER + clientSecret;
        final byte[] binaryCredentials = makeBinaryCredentials(credentials);
        return AUTHORIZATION_METHOD + Base64.encodeBase64String(binaryCredentials);
    }

    private byte[] makeBinaryCredentials(final String credentials) {
        return credentials.getBytes(StandardCharsets.UTF_8);
    }
}
