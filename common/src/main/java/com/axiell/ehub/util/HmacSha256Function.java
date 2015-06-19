package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;
import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSha256Function {
    private static final String ALGORITHM = "HmacSHA256";

    private HmacSha256Function() {
    }

    public static String hash(final String secretKey, final String data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getBytesFromHexString(secretKey), ALGORITHM);
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
        return BaseEncoding.base64().encode(mac.doFinal(data.getBytes()));
    }

    private static byte[] getBytesFromHexString(final String data) {
        return BaseEncoding.base64().decode(data);
    }
}
