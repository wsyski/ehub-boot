package com.axiell.ehub.util;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class Md5FunctionTest {
    private static final String MESSAGE = "12345678901234567890";
    private static final String EXP_DIGEST = "fd85e62d9beb45428771ec688418b271";
    private String actualDigest;

    @Test
    public void validHash() {
        whenMd5Hex();
        thenActualDigestEqualsExpectedDigest();
    }

    private void whenMd5Hex() {
        actualDigest = Md5Function.md5Hex(MESSAGE);
    }

    private void thenActualDigestEqualsExpectedDigest() {
        assertThat(actualDigest, IsEqualIgnoringCase.equalToIgnoringCase(EXP_DIGEST));
    }
}
