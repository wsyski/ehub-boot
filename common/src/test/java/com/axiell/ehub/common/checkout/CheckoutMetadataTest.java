package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.checkout.CheckoutMetadata;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        actLmsLoanId = underTest.getLmsLoanId();
    }

    private void thenActualLmsLoanIdEqualsExpectedLmsLoanId() {
        Assertions.assertEquals(LMS_LOAN_ID, actLmsLoanId);
    }
}
