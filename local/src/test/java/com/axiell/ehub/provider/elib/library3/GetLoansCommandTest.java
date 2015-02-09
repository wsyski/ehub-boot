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
    private static final String CONTENT_URL = "url";
    private GetLoansCommand underTest;
    @Mock
    private GetLoansResponse getLoansResponse;
    @Mock
    private Loan loan;
    private ContentProviderLoanMetadata loanMetadata;
    private String contentUrl;

    @Before
    public void setUp() {
        underTest = new GetLoansCommand(elibFacade, exceptionFactory);
    }

    @Test
    public void patronHasLoanWithProductId() {
        givenProductIdInPendingLoan();
        givenFormatDecorationForFormatId();
        givenContentProviderFromContentProviderConsumer();
        givenBasicCommandData();
        givenLoanWithProductId();
        givenGetLoansResponse();
        givenCommandOnPatronHasLoanWithProductId();
        whenRun();
        thenContentProviderLoanMetadataHasExpectedLoanId();
        thenContentProviderLoanMetadataHasExpectedRecordId();
        thenContentProviderLoanMetadataHasExpectedFormatDecoration();
        thenActualContentUrlEqualsExpectedUrl();
        thenActualFormatDecorationEqualsExpectedFormatDecoration();
        thenCommandIsInvoked();
    }

    private void givenProductIdInPendingLoan() {
        given(pendingLoan.contentProviderRecordId()).willReturn(PRODUCT_ID);
    }

    private void givenCommandOnPatronHasLoanWithProductId() {
        underTest.on(PATRON_HAS_LOAN_WITH_PRODUCT_ID, next);
    }

    private void thenContentProviderLoanMetadataHasExpectedRecordId() {
        assertEquals(PRODUCT_ID, loanMetadata.getRecordId());
    }

    private void thenContentProviderLoanMetadataHasExpectedLoanId() {
        assertEquals(LOAN_ID, loanMetadata.getId());
    }

    private void thenContentProviderLoanMetadataHasExpectedFormatDecoration() {
        assertEquals(formatDecoration, loanMetadata.getFormatDecoration());
    }

    private void thenActualContentUrlEqualsExpectedUrl() {
        assertEquals(CONTENT_URL, contentUrl);
    }

    private void thenActualFormatDecorationEqualsExpectedFormatDecoration() {
        assertEquals(formatDecoration, loanMetadata.getFormatDecoration());
    }

    private void givenLoanWithProductId() {
        given(loan.getLoanId()).willReturn(LOAN_ID);
        given(loan.getProductId()).willReturn(PRODUCT_ID);
        given(loan.getExpirationDate()).willReturn(new Date());
        given(loan.getContentUrlFor(anyString())).willReturn(CONTENT_URL);
        given(getLoansResponse.getLoanWithProductId(anyString())).willReturn(loan);
    }

    private void givenGetLoansResponse() {
        given(elibFacade.getLoans(contentProviderConsumer, patron)).willReturn(getLoansResponse);
    }

    private void whenRun() {
        underTest.run(data);
        loanMetadata = data.getContentProviderLoanMetadata();
        contentUrl = data.getContentUrl();
    }

    @Test
    public void patronHasNoLoanWithProductId() {
        givenBasicCommandData();
        givenGetLoansResponse();
        givenCommandOnPatronHasNoLoanWithProductId();
        whenRun();
        thenContentProviderLoanMetadataIsNull();
        thenContentUrlIsNull();
        thenCommandIsInvoked();
    }

    private void thenContentUrlIsNull() {
        assertNull(contentUrl);
    }

    private void givenCommandOnPatronHasNoLoanWithProductId() {
        underTest.on(PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID, next);
    }

    private void thenContentProviderLoanMetadataIsNull() {
        assertNull(loanMetadata);
    }
}
