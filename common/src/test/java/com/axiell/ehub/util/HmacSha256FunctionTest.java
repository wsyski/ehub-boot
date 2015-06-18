package com.axiell.ehub.util;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class HmacSha256FunctionTest {
    private static final String KEY = "8271ee62ad72c5e7c7b8ddbb6898835f64d2bb93";
    private static final String DATA = "1001\n1200012512\n1427151942";
    private static final String EXP_HASH = "qLD18IaZbz1P5v5KBFtaSTfKS4y5bNCYHb8X89L1N6M=";
    private String actualHash;

    @Test
    public void validHash() {
        whenHmacSha256Hash();
        thenActualHexEqualsExpectedHex();
    }

    private void whenHmacSha256Hash() {
        actualHash = HmacSha256Function.hash(KEY, DATA);
    }

    private void thenActualHexEqualsExpectedHex() {
        assertThat(actualHash, IsEqualIgnoringCase.equalToIgnoringCase(EXP_HASH));
    }
}
