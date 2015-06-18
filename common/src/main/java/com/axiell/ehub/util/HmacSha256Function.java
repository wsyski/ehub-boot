package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSha256Function {

    private HmacSha256Function() {
    }

    public static String hash(final String secretKey, final String data) {
        try {
            String algorithm="HmacSHA256";
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF8"), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);
            return Base64.encodeBase64String(mac.doFinal(data.getBytes("UTF8")));
        } catch (NoSuchAlgorithmException | InvalidKeyException |UnsupportedEncodingException ex) {
            throw new InternalServerErrorException("Could not get a hash for the HmacSHA256 algorithm", ex);
        }
    }

}
