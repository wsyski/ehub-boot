/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**
 *
 */
public class HmacSHA1HashFunctionTest {

    /**
     * Test method for {@link HmacSha1Function#hmacSha1(byte[], byte[])}.
     *
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testHmacSha1() throws UnsupportedEncodingException {
        byte[] input = "input".getBytes(StandardCharsets.UTF_8);
        byte[] key1 = "key1".getBytes(StandardCharsets.UTF_8);
        byte[] key2 = "key1".getBytes(StandardCharsets.UTF_8);
        byte[] key3 = "key3".getBytes(StandardCharsets.UTF_8);

        byte[] digest1 = HmacSha1Function.hmacSha1(input, key1);
        byte[] digest2 = HmacSha1Function.hmacSha1(input, key2);
        byte[] digest3 = HmacSha1Function.hmacSha1(input, key3);

        Assertions.assertTrue(MessageDigest.isEqual(digest1, digest2));
        Assertions.assertFalse(MessageDigest.isEqual(digest1, digest3));
        Assertions.assertFalse(MessageDigest.isEqual(digest2, digest3));
    }

}
