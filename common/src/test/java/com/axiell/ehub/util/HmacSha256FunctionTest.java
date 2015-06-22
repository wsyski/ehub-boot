package com.axiell.ehub.util;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class HmacSha256FunctionTest {
    private static final String KEY = "nbYJzxJ2dg/WO0KVYz6T9n4n1hRCpgui8DCIm5uFRbE=";
    private static final String DATA = "4390\nehub1\n1434616780";
    private static final String EXP_HASH = "Y1ayI8QqC8MFr4TbMYGqsJswIWx9SM0jMSU/uj5aLbA=";
    private String actualHash;

    @Test
    public void validHash() {
        whenHmacSha256Hash();
        thenActualHexEqualsExpectedHex();
    }

    private void whenHmacSha256Hash() {
        actualHash = HmacSha256Function.digest(KEY, DATA);
    }

    private void thenActualHexEqualsExpectedHex() {
        assertThat(actualHash, IsEqualIgnoringCase.equalToIgnoringCase(EXP_HASH));
    }
}
