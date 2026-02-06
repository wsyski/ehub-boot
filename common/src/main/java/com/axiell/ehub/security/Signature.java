/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import static com.axiell.ehub.security.HmacSha1Function.hmacSha1;
import static com.axiell.ehub.util.EhubUrlCodec.authInfoEncode;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

/**
 * Represents the signature of a request to the Axiell eHUB.
 */
@Slf4j
public final class Signature {
    private final byte[] digest;

    public Signature(final List<String> signatureItems, final String secretKey) {
        final String baseString = makeBaseString(signatureItems);
        final byte[] input = getBytesInUtf8(baseString);
        final byte[] key = getBytesInUtf8(secretKey);
        digest = hmacSha1(input, key);
    }

    private static byte[] getBytesInUtf8(final String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    private String makeBaseString(final List<String> signatureItems) {
        final StringBuilder builder = new StringBuilder();
        for (final String signatureItem : signatureItems) {
            appendParam(signatureItem, builder);
        }
        String baseString = builder.toString();
        log.debug("baseString: " + baseString);
        return baseString;
    }

    private void appendParam(final String param, final StringBuilder builder) {
        if (param != null) {
            final String encodedParam = authInfoEncode(param);
            builder.append(encodedParam);
        }
    }

    /**
     * Returns the bas64 and URL encoded version of this {@link Signature}.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        if (digest == null) {
            return StringUtils.EMPTY;
        }
        return encodeBase64String(digest);
    }

    public boolean isValid(final String actualBase64Digest) {
        logSignatures(actualBase64Digest);
        byte[] expectedDigest = decodeBase64(actualBase64Digest);
        return MessageDigest.isEqual(expectedDigest, digest);
    }

    private void logSignatures(final String actualBase64EncodedDigest) {
        log.debug("Actual Base64 encoded digest: '" + actualBase64EncodedDigest + "'");
        log.debug("Expected Base64 encoded digest: '" + toString() + "'");
    }
}
