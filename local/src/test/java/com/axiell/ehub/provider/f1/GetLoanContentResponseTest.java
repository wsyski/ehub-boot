package com.axiell.ehub.provider.f1;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetLoanContentResponseTest {
    private static final String EXP_CONTENT = "/media";
    private GetLoanContentResponse underTest;
    private String providedValue;
    private String actualValue;

    @Test
    public void validContent() {
        givenValidContent();
        givenGetLoanContentResponse();
        whenGetValue();
        thenActualContentEqualsExpectedContent();
        thenContentIsValid();
    }

    @Test
    public void invalidContent_emptyValue() {
        givenEmptyValue();
        givenGetLoanContentResponse();
        whenGetValue();
        thenContentIsInvalid();
    }

    @Test
    public void invalidContent_nullValue() {
        givenNullValue();
        givenGetLoanContentResponse();
        whenGetValue();
        thenContentIsInvalid();
    }

    private void givenValidContent() {
        providedValue = EXP_CONTENT;
    }

    private void thenActualContentEqualsExpectedContent() {
        assertEquals(EXP_CONTENT, actualValue);
    }

    private void thenContentIsValid() {
        assertTrue(underTest.isValidContent());
    }

    private void givenEmptyValue() {
        providedValue = "";
    }

    private void givenNullValue() {
        providedValue = null;
    }

    private void givenGetLoanContentResponse() {
        underTest = new GetLoanContentResponse(providedValue);
    }

    private void whenGetValue() {
        actualValue = underTest.getValue();
    }

    private void thenContentIsInvalid() {
        assertFalse(underTest.isValidContent());
    }
}
