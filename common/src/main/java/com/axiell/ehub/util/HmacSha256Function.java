package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSha256Function {

    private HmacSha256Function() {
    }

    public static String hash(final String key, final String message) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            return Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new InternalServerErrorException("Could not get a hash for the HmacSHA256 algorithm", ex);
        }
    }

}
