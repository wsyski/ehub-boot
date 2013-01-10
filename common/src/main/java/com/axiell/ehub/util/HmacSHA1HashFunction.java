/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Provides the possibility to create HMAC-SHA1 hashes.
 */
public final class HmacSHA1HashFunction {
    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * Private constructor that prevents direct instantiation.
     */
    private HmacSHA1HashFunction() {
    }
    

    
    /**
     * Creates an HMAC-SHA1 hash from the given input.
     * 
     * @param input the input to create the hash from
     * @param key the key to initialize the underlying {@link Mac} with
     * @return a HMAC-SHA1 digest
     */
    public static byte[] hmacSha1(final byte[] input, final byte[] key) {
        final SecretKey secretKey = new SecretKeySpec(key, HMAC_SHA1);         
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(secretKey);
            return mac.doFinal(input);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new InternalServerErrorException("Could not create an HMAC-SHA1 hash", e);
        }
    }
    
}
