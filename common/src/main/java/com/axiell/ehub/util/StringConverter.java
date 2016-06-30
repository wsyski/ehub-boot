package com.axiell.ehub.util;

import com.google.common.io.BaseEncoding;

import java.nio.charset.StandardCharsets;

public class StringConverter {

    private StringConverter() {
    }

    public static byte[] getBytesInUtf8(final String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] base64Decode(final String input) {
        return BaseEncoding.base64().decode(input);
    }

    public static String base64Encode(final byte[] input) {
        return BaseEncoding.base64().encode(input);
    }
}
