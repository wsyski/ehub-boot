/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.junit.Test;

/**
 * 
 */
public class HashFunctionTest {

    /**
     * Test method for {@link com.axiell.ehub.util.HashFunction#hmacSha1(byte[], byte[])}.
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testHmacSha1() throws UnsupportedEncodingException {
        byte[] input = "input".getBytes("UTF8");
        byte[] key1 = "key1".getBytes("UTF8");
        byte[] key2 = "key1".getBytes("UTF8");
        byte[] key3 = "key3".getBytes("UTF8");
        
       byte[] digest1 = HashFunction.hmacSha1(input, key1);
       byte[] digest2 = HashFunction.hmacSha1(input, key2);
       byte[] digest3 = HashFunction.hmacSha1(input, key3);
       
       assertTrue(MessageDigest.isEqual(digest1, digest2));
       assertFalse(MessageDigest.isEqual(digest1, digest3));
       assertFalse(MessageDigest.isEqual(digest2, digest3));
    }

}
