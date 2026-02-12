package com.axiell.ehub.common.util;

import com.axiell.ehub.common.InternalServerErrorException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSha256Function {
    private static final String ALGORITHM = "HmacSHA256";

    private HmacSha256Function() {
    }

    public static byte[] hmacSha256(final byte[] input, final byte[] secretKey) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, ALGORITHM);
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
        return mac.doFinal(input);
    }
}
