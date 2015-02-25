package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentFactory;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CreateLoanCommandChainTest {
    private static final Date EXPIRATION_DATE = new Date();
    private static final String CONTENT_URL = "CONTENT_URL";
    private static final String CP_LOAN_ID = "CP_LOAN_ID";
    private static final String CP_RECORD_ID = "CP_RECORD_ID";

    private CreateLoanCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private IContentFactory contentFactory;
    @Mock
    private CommandData commandData;
    @Mock
    private PendingLoan pendingLoan;
    @Mock
    private BookAvailability bookAvailability;
    @Mock
    private CreatedLoan createdLoan;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private ContentProviderLoan contentProviderLoan;
    @Mock
    private ContentProviderLoanMetadata loanMetadata;
    @Mock
    private ContentLink contentLink;
    @Mock
    private Patron patron;
    @Mock
    private GetLoansResponse getLoansResponse;
    @Mock
    private Loan loan;

    private ContentProviderLoan actualLoan;

    @Before
    public void setUpUnderTest() {
        underTest = new CreateLoanCommandChain(elibFacade, exceptionFactory, contentFactory);
    }

    @Before
    public void setUpContentProviderLoanWithDefaultData() {
        given(contentProviderLoan.expirationDate()).willReturn(EXPIRATION_DATE);
        given(contentLink.href()).willReturn(CONTENT_URL);
        given(contentProviderLoan.contentLink()).willReturn(contentLink);
        given(loanMetadata.getContentProvider()).willReturn(contentProvider);
        given(loanMetadata.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(loanMetadata.getFormatDecoration()).willReturn(formatDecoration);
        given(loanMetadata.getId()).willReturn(CP_LOAN_ID);
        given(loanMetadata.getRecordId()).willReturn(CP_RECORD_ID);
        given(contentProviderLoan.getMetadata()).willReturn(loanMetadata);
    }

    @Test
    public void execute_patronHasNoLoanWithProductId() {
        givenGetLoansResponse();
        givenCommandData();
        givenProductIsAvailable();
        givenCreatedLoan();
        givenCreatedDownloadableContent();
        whenExecute();
        thenActualLoanEqualsToExpectedLoan();
    }

    private void givenGetLoansResponse() {
        given(elibFacade.getLoans(any(ContentProviderConsumer.class), any(Patron.class))).willReturn(getLoansResponse);
    }

    private void givenCommandData() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(pendingLoan.contentProviderRecordId()).willReturn(CP_RECORD_ID);
        given(patron.hasId()).willReturn(true);
        given(pendingLoan.contentProviderFormatId()).willReturn("contentProviderFormatId");
        commandData = CommandData.newInstance(contentProviderConsumer, patron, "sv").setPendingLoan(pendingLoan);
    }

    private void givenProductIsAvailable() {
        given(bookAvailability.isProductAvailable(any(String.class))).willReturn(true);
        given(elibFacade.getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(Patron.class))).willReturn(bookAvailability);
    }

    private void givenCreatedLoan() {
        given(createdLoan.getContentUrlFor(any(String.class))).willReturn(CONTENT_URL);
        given(createdLoan.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(createdLoan.getLoanId()).willReturn(CP_LOAN_ID);
        given(elibFacade.createLoan(any(ContentProviderConsumer.class), any(String.class), any(Patron.class))).willReturn(createdLoan);
    }

    private void givenCreatedDownloadableContent() {
        given(contentFactory.create(any(String.class), any(FormatDecoration.class))).willReturn(contentLink);
    }

    private void whenExecute() {
        actualLoan = underTest.execute(commandData);
    }

    private void thenActualLoanEqualsToExpectedLoan() {
        assertThat(actualLoan.contentLink(), is(contentProviderLoan.contentLink()));
        assertThat(actualLoan.expirationDate(), is(contentProviderLoan.expirationDate()));
        assertThat(actualLoan.getMetadata().getContentProvider(), is(contentProviderLoan.getMetadata().getContentProvider()));
        assertThat(actualLoan.getMetadata().getExpirationDate(), is(contentProviderLoan.getMetadata().getExpirationDate()));
        assertThat(actualLoan.getMetadata().getFormatDecoration(), is(contentProviderLoan.getMetadata().getFormatDecoration()));
        assertThat(actualLoan.getMetadata().getId(), is(contentProviderLoan.getMetadata().getId()));
        assertThat(actualLoan.getMetadata().getRecordId(), is(contentProviderLoan.getMetadata().getRecordId()));
    }

    @Test
    public void execute_patronHasLoanWithProductId() {
        givenLoanWithProductIdInGetLoansResponse();
        givenGetLoansResponse();
        givenCommandData();
        givenCreatedDownloadableContent();
        whenExecute();
        thenActualLoanEqualsToExpectedLoan();
    }

    private void givenLoanWithProductIdInGetLoansResponse() {
        given(loan.getLoanId()).willReturn(CP_LOAN_ID);
        given(loan.getProductId()).willReturn(CP_RECORD_ID);
        given(loan.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(getLoansResponse.getLoanWithProductId(anyString())).willReturn(loan);
    }
}
