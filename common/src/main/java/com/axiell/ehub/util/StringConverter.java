package com.axiell.ehub.util;

import com.axiell.ehub.InternalServerErrorException;
import com.google.common.io.BaseEncoding;

import java.io.UnsupportedEncodingException;

public class StringConverter {
    private static final String UTF8 = "UTF-8";

    private StringConverter() {
    }

    public static byte[] getBytesInUtf8(final String input) {
        try {
            return input.getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException("Could not convert input in '" + UTF8 + "' encoding", e);
        }
    }

    public static byte[] base64Decode(final String input) {
        return BaseEncoding.base64().decode(input);
    }

    public static String  base64Encode(final byte[] input) {
        return BaseEncoding.base64().encode(input);
    }
}
