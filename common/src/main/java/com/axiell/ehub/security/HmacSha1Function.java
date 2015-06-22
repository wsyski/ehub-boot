/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import com.axiell.ehub.InternalServerErrorException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Provides the possibility to create HMAC-SHA1 hashes.
 */
public final class HmacSha1Function {
    private static final String ALGORITHM = "HmacSHA1";

    /**
     * Private constructor that prevents direct instantiation.
     */
    private HmacSha1Function() {
    }

    /**
     * Creates an HMAC-SHA1 digest from the given input.
     *
     * @param input the input to create the digest from
     * @param secretKey   the key to initialize the underlying {@link Mac} with
     * @return a HMAC-SHA1 digest
     */
    public static byte[] hmacSha1(final byte[] input, final byte[] secretKey) {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, ALGORITHM);
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKeySpec);
            return mac.doFinal(input);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new InternalServerErrorException("Could not create an HMAC-SHA1 digest", ex);
        }
    }

}
