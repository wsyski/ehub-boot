package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSha256Function {

    private HmacSha256Function() {
    }

    public static String hash(final String secretKeyAsHexString, final String data) {
        String algorithm = "HmacSHA256";
        String charset = "UTF8";
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKeyAsHexString.toCharArray());
        } catch (DecoderException ex) {
            throw new InternalServerErrorException("Could not parse hex string: " + secretKeyAsHexString, ex);
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, algorithm);

        byte[] dataBytes;
        try {
            dataBytes = data.getBytes(charset);
        } catch (UnsupportedEncodingException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
        Mac mac;
        try {
            mac = Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException ex) {
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
        return Base64.encodeBase64String(mac.doFinal(dataBytes));
    }

}
