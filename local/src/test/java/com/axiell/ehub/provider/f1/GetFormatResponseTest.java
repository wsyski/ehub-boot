package com.axiell.ehub.provider.f1;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetFormatResponseTest {
    private static final String EXP_TYPE_ID = "3";
    private GetFormatResponse underTest;
    private String providedValue;
    private String actualValue;

    @Test
    public void validTypeId() {
        givenTypeIdAsValue();
        givenGetFormatResponse();
        whenGetTypeId();
        thenActualValueEqualsExpectedTypeId();
        thenGetFormatResponseContainsValidFormat();
    }

    @Test
    public void noSuchFormat() {
        givenNoSuchFormatAsValue();
        givenGetFormatResponse();
        whenGetTypeId();
        thenGetFormatResponeContainsNoValidFormat();
    }

    private void givenNoSuchFormatAsValue() {
        providedValue = GetFormatResponse.NO_SUCH_FORMAT;
    }

    private void thenGetFormatResponeContainsNoValidFormat() {
        assertFalse(underTest.isValidFormat());
    }

    private void thenActualValueEqualsExpectedTypeId() {
        assertEquals(EXP_TYPE_ID, actualValue);
    }

    private void givenTypeIdAsValue() {
        providedValue = EXP_TYPE_ID;
    }

    private void thenGetFormatResponseContainsValidFormat() {
        assertTrue(underTest.isValidFormat());
    }

    private void givenGetFormatResponse() {
        underTest = new GetFormatResponse(providedValue);
    }

    private void whenGetTypeId() {
        actualValue = underTest.getValue();
    }
}
