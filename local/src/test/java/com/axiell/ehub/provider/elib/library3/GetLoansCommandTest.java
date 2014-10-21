package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Date;

import static com.axiell.ehub.provider.elib.library3.GetLoansCommand.Result.PATRON_HAS_LOAN_WITH_PRODUCT_ID;
import static com.axiell.ehub.provider.elib.library3.GetLoansCommand.Result.PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

public class GetLoansCommandTest extends AbstractElib3CommandTest {
    private static final String LOAN_ID = "1";
    private GetLoansCommand underTest;
    @Mock
    private GetLoansResponse getLoansResponse;
    @Mock
    private Loan loan;
    private ContentProviderLoanMetadata loanMetadata;

    @Before
    public void setUp() {
        underTest = new GetLoansCommand(elibFacade, exceptionFactory);
    }

    @Test
    public void patronHasLoanWithProductId() {
        givenProductIdInPendingLoan();
        givenFormatDecorationForFormatId();
        givenBasicCommandData();
        givenLoanWithProductId();
        givenGetLoansResponse();
        givenCommandOnPatronHasLoanWithProductId();
        whenRun();
        thenContentProviderLoanMetdataHasExpectedLoanId();
        thenContentProviderLoanMetdataHasExpectedRecordId();
        thenCommandIsInvoked();
    }

    private void givenProductIdInPendingLoan() {
        given(pendingLoan.getContentProviderRecordId()).willReturn(PRODUCT_ID);
    }

    private void givenCommandOnPatronHasLoanWithProductId() {
        underTest.on(PATRON_HAS_LOAN_WITH_PRODUCT_ID, next);
    }

    private void thenContentProviderLoanMetdataHasExpectedRecordId() {
        assertEquals(PRODUCT_ID, loanMetadata.getRecordId());
    }

    private void thenContentProviderLoanMetdataHasExpectedLoanId() {
        assertEquals(LOAN_ID, loanMetadata.getId());
    }

    private void givenLoanWithProductId() {
        given(loan.getLoanId()).willReturn(LOAN_ID);
        given(loan.getProductId()).willReturn(PRODUCT_ID);
        given(loan.getExpirationDate()).willReturn(new Date());
        given(getLoansResponse.getLoanWithProductId(anyString())).willReturn(loan);
    }

    private void givenGetLoansResponse() {
        given(elibFacade.getLoans(contentProviderConsumer, patron)).willReturn(getLoansResponse);
    }

    private void whenRun() {
        underTest.run(data);
        loanMetadata = data.getContentProviderLoanMetadata();
    }

    @Test
    public void patronHasNoLoanWithProductId() {
        givenBasicCommandData();
        givenGetLoansResponse();
        givenCommandOnPatronHasNoLoanWithProductId();
        whenRun();
        thenContentProviderLoanMetadataIsNull();
        thenCommandIsInvoked();
    }

    private void givenCommandOnPatronHasNoLoanWithProductId() {
        underTest.on(PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID, next);
    }

    private void thenContentProviderLoanMetadataIsNull() {
        assertNull(loanMetadata);
    }
}
