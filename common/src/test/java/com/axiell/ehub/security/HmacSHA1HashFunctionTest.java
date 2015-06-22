/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.security;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.junit.Test;

/**
 * 
 */
public class HmacSHA1HashFunctionTest {

    /**
     * Test method for {@link HmacSha1Function#hmacSha1(byte[], byte[])}.
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testHmacSha1() throws UnsupportedEncodingException {
        byte[] input = "input".getBytes("UTF8");
        byte[] key1 = "key1".getBytes("UTF8");
        byte[] key2 = "key1".getBytes("UTF8");
        byte[] key3 = "key3".getBytes("UTF8");
        
       byte[] digest1 = HmacSha1Function.hmacSha1(input, key1);
       byte[] digest2 = HmacSha1Function.hmacSha1(input, key2);
       byte[] digest3 = HmacSha1Function.hmacSha1(input, key3);
       
       assertTrue(MessageDigest.isEqual(digest1, digest2));
       assertFalse(MessageDigest.isEqual(digest1, digest3));
       assertFalse(MessageDigest.isEqual(digest2, digest3));
    }

}
