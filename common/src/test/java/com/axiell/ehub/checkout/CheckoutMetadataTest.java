package com.axiell.ehub.checkout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutMetadataTest {
    private static final String LMS_LOAN_ID = "lmsLoanId";
    private CheckoutMetadata underTest;
    @Mock
    private CheckoutMetadataDTO dto;
    private String actLmsLoanId;

    @Test
    public void getLmsLoanId() {
        givenLmsLoanIdFromDTO();
        givenCheckoutInstance();
        whenGetLmsLoanId();
        thenActualLmsLoanIdEqualsExpectedLmsLoanId();
    }

    private void givenLmsLoanIdFromDTO() {
        given(dto.getLmsLoanId()).willReturn(LMS_LOAN_ID);
    }

    private void givenCheckoutInstance() {
        underTest = new CheckoutMetadata(dto);
    }

    private void whenGetLmsLoanId() {
        actLmsLoanId = underTest.lmsLoanId();
    }

    private void thenActualLmsLoanIdEqualsExpectedLmsLoanId() {
        assertEquals(LMS_LOAN_ID, actLmsLoanId);
    }
}
