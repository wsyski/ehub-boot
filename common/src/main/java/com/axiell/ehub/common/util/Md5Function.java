package com.axiell.ehub.common.util;

import org.apache.commons.codec.digest.DigestUtils;

import static com.axiell.ehub.common.util.StringConverter.getBytesInUtf8;

public class Md5Function {

    private Md5Function() {
    }

    public static String md5Hex(final String input) {
        final byte[] inputAsBytes = getBytesInUtf8(input);
        return DigestUtils.md5Hex(inputAsBytes);
    }
}
