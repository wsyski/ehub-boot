package com.axiell.ehub.util;

import junit.framework.Assert;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class SHA512FunctionTest {
    private static final String EXP_INPUT = "123abc";
    private static final String EXP_HEX = "7B6AD79B346FB6951275343948E13C1B4EBCA82A5452A6C5D15684377F096CA927506A23A847E6E046061399631B16FC2820C8B0E02D0EA87AA5A203A77C2A7E";
    private String actualHex;

    @Test
    public void validHex() {
        whenSha512Hex();
        thenActualHexEqualsExpectedHex();
    }

    private void whenSha512Hex() {
        actualHex = SHA512Function.sha512Hex(EXP_INPUT);
    }

    private void thenActualHexEqualsExpectedHex() {
        assertThat(actualHex, IsEqualIgnoringCase.equalToIgnoringCase(EXP_HEX));
    }
}
