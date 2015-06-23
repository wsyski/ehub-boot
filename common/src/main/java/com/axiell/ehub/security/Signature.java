/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.List;

import static com.axiell.ehub.security.HmacSha1Function.hmacSha1;
import static com.axiell.ehub.util.EhubUrlCodec.encode;
import static com.axiell.ehub.util.StringConverter.getBytesInUtf8;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

/**
 * Represents the signature of a request to the Axiell eHUB.
 */
public final class Signature {
    private static final Logger LOGGER = LoggerFactory.getLogger(Signature.class);
    private final byte[] digest;

    public Signature(final String base64EncodedSignature) {
        digest = decodeBase64(base64EncodedSignature);
    }

    public Signature(final List<?> signatureItems, final String secretKey) {
        final String baseString = makeBaseString(signatureItems);
        final byte[] input = getBytesInUtf8(baseString);
        final byte[] key = getBytesInUtf8(secretKey);
        digest = hmacSha1(input, key);
    }

    private String makeBaseString(final List<?> signatureItems) {
        final StringBuilder builder = new StringBuilder();
        for (final Object signatureItem : signatureItems) {
            if (signatureItem instanceof String) {
                appendParam(String.class.cast(signatureItem), builder);
            } else {
                builder.append(signatureItem);
            }
        }
        return builder.toString();
    }

    private void appendParam(String param, StringBuilder builder) {
        if (param != null) {
            final String encodedParam = encode(param);
            builder.append(encodedParam);
        }
    }

    /**
     * Returns the bas64 and URL encoded version of this {@link Signature}.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String base64Signature = encodeBase64String(digest);
        return encode(base64Signature);
    }

    byte[] getDigest() {
        return digest;
    }

    /**
     * Verifies whether this {@link Signature} is valid by comparing it to the provided signature.
     *
     * @param expectedSignature the expected {@link Signature} to compare this {@link Signature} against
     * @return <code>true</code> if and only if the digest of this {@link Signature} is equal to the digest of the
     * provided {@link Signature}, <code>false</code> otherwise
     */
    public boolean isValid(Signature expectedSignature) {
        logSignatures(expectedSignature);
        byte[] expectedDigest = expectedSignature.getDigest();
        return MessageDigest.isEqual(expectedDigest, digest);
    }

    private void logSignatures(final Signature expectedSignature) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Expected signature = '" + expectedSignature.toString() + "'");
            LOGGER.debug("Actual signature   = '" + toString() + "'");
        }
    }
}
