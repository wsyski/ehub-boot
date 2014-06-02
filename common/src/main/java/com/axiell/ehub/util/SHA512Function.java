package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static com.axiell.ehub.util.StringConverter.*;

public class SHA512Function {

    private SHA512Function() {
    }

    public static String sha512Hex(final String input) {
        final byte[] inputAsBytes = getBytesInUtf8(input);
        final byte[] digest = digest(inputAsBytes);
        return encodeHex(digest);
    }

    private static byte[] digest(final byte[] input) {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException("Could not get a MessageDigest for the SHA-512 algorithm", e);
        }
        return messageDigest.digest(input);
    }

    private static String encodeHex(final byte[] digest) {
        final char[] hex = Hex.encodeHex(digest);
        return new String(hex);
    }
}
