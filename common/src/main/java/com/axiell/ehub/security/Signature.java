/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import static com.axiell.ehub.security.HmacSHA1HashFunction.hmacSha1;
import static com.axiell.ehub.util.EhubUrlCodec.encode;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.util.EhubCharsets;

/**
 * Represents the signature of a request to the Axiell eHUB.
 */
public final class Signature {
    private static final Logger LOGGER = LoggerFactory.getLogger(Signature.class);    
    private final byte[] digest;

    /**
     * Base64 decodes the provided signature value to a digest.
     * 
     * @param base64EncodedSignature the base64 encoded signature
     */
    public Signature(String base64EncodedSignature) {
        this.digest = decodeBase64(base64EncodedSignature);
    }

    /**
     * Computes a digest by concatenating the parameters to a base string and then using the HMAC-SHA1 algorithm with
     * the secret key of the {@link EhubConsumer} as key to create a digest.
     * 
     * @param ehubConsumerId the non-null ID of an {@link EhubConsumer}
     * @param ehubConsumerSecretKey the non-null secret key of an {@link EhubConsumer}
     * @param libraryCard the library card, can be <code>null</code>
     * @param pin the pin, can be <code>null</code>
     */
    public Signature(final Long ehubConsumerId, final String ehubConsumerSecretKey, final String libraryCard, final String pin) {
        final StringBuilder builder = new StringBuilder().append(ehubConsumerId);

        if (libraryCard != null) {
            final String encodedLibraryCard = encode(libraryCard);
            builder.append(encodedLibraryCard);
        }

        if (pin != null) {
            final String encodedPin = encode(pin);
            builder.append(encodedPin);
        }

        final String baseString = builder.toString();
        final byte[] input;
        final byte[] key;

        try {
            input = baseString.getBytes(EhubCharsets.UTF_8);
            key = ehubConsumerSecretKey.getBytes(EhubCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException("Could not get the bytes of strings in '" + EhubCharsets.UTF_8 + "' encoding", e);
        }

        this.digest = hmacSha1(input, key);
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

    /**
     * Returns the digest of this {@link Signature}.
     * 
     * @return the digest of this {@link Signature}
     */
    public byte[] getDigest() {
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
