/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.axiell.ehub.InternalServerErrorException;

/**
 * Provides the possibility to create hashes using different algorithms.
 */
public final class HashFunction {
    private static final int POS_SIGNUM = 1;
    private static final int HEXADECIMAL_RADIX = 16;
    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * Private constructor that prevents direct instantiation.
     */
    private HashFunction() {
    }
    
    /**
     * Creates an MD5 hash from the given input.
     * 
     * @param input the input to create the hash from
     * @return a hexadecimal value 32 characters long
     * @throws HashFunctionRuntimeException if the MD5 algorithm is not available
     */
    public static String md5(final byte[] input) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException("MD5 algorithm is not available", e);
        }
        byte[] hashValue = messageDigest.digest(input);
        BigInteger hashNumber = new BigInteger(POS_SIGNUM, hashValue);
        // Prepend a zero to get a "proper" MD5 hash value
        StringBuilder signatureBuilder = new StringBuilder('0');
        signatureBuilder.append(hashNumber.toString(HEXADECIMAL_RADIX));
        return signatureBuilder.toString();
    }
    
    /**
     * Creates an HMAC-SHA1 hash from the given input.
     * 
     * @param input the input to create the hash from
     * @param key the key to initialize the underlying {@link Mac} with
     * @return a HMAC-SHA1 digest
     * @throws HashFunctionRuntimeException if the SHA algorithm is not available
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
