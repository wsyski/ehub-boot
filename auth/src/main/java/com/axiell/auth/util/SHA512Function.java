package com.axiell.auth.util;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA512Function {

    private SHA512Function() {
    }

    public static String sha512Hex(final String input) {
        final byte[] inputAsBytes = input.getBytes(StandardCharsets.UTF_8);
        final byte[] digest = digest(inputAsBytes);
        return encodeHex(digest);
    }

    private static byte[] digest(final byte[] input) {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex) {
            throw new AuthRuntimeException(ex.getMessage(), ex);
        }
        return messageDigest.digest(input);
    }

    private static String encodeHex(final byte[] digest) {
        final char[] hex = Hex.encodeHex(digest);
        return new String(hex);
    }
}
