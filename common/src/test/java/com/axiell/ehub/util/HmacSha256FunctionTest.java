package com.axiell.ehub.util;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class HmacSha256FunctionTest {
    private static final String KEY = "secret";
    private static final String MESSAGE = "123abc";
    private static final String EXP_HASH = "9jMfRrNYWsoYj8tiNw+WQYOL0GOz+sDv7WsCvwS3/yA=";
    private String actualHash;

    @Test
    public void validHash() {
        whenHmacSha256Hash();
        thenActualHexEqualsExpectedHex();
    }

    private void whenHmacSha256Hash() {
        actualHash = HmacSha256Function.hash(KEY,MESSAGE);
    }

    private void thenActualHexEqualsExpectedHex() {
        assertThat(actualHash, IsEqualIgnoringCase.equalToIgnoringCase(EXP_HASH));
    }
}
