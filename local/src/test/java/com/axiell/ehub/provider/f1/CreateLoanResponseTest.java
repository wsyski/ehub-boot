package com.axiell.ehub.provider.f1;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateLoanResponseTest {
    private static final String EXP_LOAN_ID = "123456";
    private CreateLoanResponse underTest;
    private String providedValue;
    private String actualValue;

    @Test
    public void validLoan() {
        givenLoanIdAsValue();
        givenCreateLoanResponse();
        whenGetValue();
        thenActualValueEqualsExpectedLoanId();
        thenLoanIsValid();
    }

    @Test
    public void invalidLoan() {
        givenErrorAsValue();
        givenCreateLoanResponse();
        whenGetValue();
        thenLoanIsNotValid();
    }

    private void thenActualValueEqualsExpectedLoanId() {
        assertEquals(EXP_LOAN_ID, actualValue);
    }

    private void thenLoanIsValid() {
        assertTrue(underTest.isValidLoan());
    }

    private void thenLoanIsNotValid() {
        assertFalse(underTest.isValidLoan());
    }

    private void givenLoanIdAsValue() {
        providedValue = EXP_LOAN_ID;
    }

    private void givenErrorAsValue() {
        providedValue = "ERROR";
    }

    private void givenCreateLoanResponse() {
        underTest = new CreateLoanResponse(providedValue);
    }

    private void whenGetValue() {
        actualValue = underTest.getValue();
    }
}
