package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_KEY;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_CLIENT_SECRET;
import static com.axiell.ehub.util.EhubCharsets.UTF_8;

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
        try {
            return credentials.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException("Could not get the bytes of strings in '" + UTF_8 + "' encoding", e);
        }
    }
}
