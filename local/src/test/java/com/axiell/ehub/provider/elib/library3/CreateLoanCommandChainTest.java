package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.PendingLoan;
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
    private ContentProviderLoan loan;
    @Mock
    private ContentProviderLoanMetadata loanMetadata;
    @Mock
    private DownloadableContent downloadableContent;

    private ContentProviderLoan actualLoan;

    @Before
    public void setUpUnderTest() {
        underTest = new CreateLoanCommandChain(elibFacade, exceptionFactory, contentFactory);
    }

    @Before
    public void setUpContentProviderLoanWithDefaultData() {
        given(loan.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(downloadableContent.getUrl()).willReturn(CONTENT_URL);
        given(loan.getContent()).willReturn(downloadableContent);
        given(loanMetadata.getContentProvider()).willReturn(contentProvider);
        given(loanMetadata.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(loanMetadata.getFormatDecoration()).willReturn(formatDecoration);
        given(loanMetadata.getId()).willReturn(CP_LOAN_ID);
        given(loanMetadata.getRecordId()).willReturn(CP_RECORD_ID);
        given(loan.getMetadata()).willReturn(loanMetadata);
    }

    @Test
    public void execute() {
        givenCommandData();
        givenProductIsAvailable();
        givenCreatedLoan();
        givenCreatedDownloadableContent();
        whenExecute();
        thenActualLoanEqualsToExpectedLoan();
    }

    private void givenCommandData() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(pendingLoan.getContentProviderRecordId()).willReturn(CP_RECORD_ID);
        commandData = CommandData.newInstance(contentProviderConsumer, "libraryCard").setPendingLoan(pendingLoan);
    }

    private void givenProductIsAvailable() {
        given(bookAvailability.isProductAvailable(any(String.class))).willReturn(true);
        given(elibFacade.getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(String.class))).willReturn(bookAvailability);
    }

    private void givenCreatedLoan() {
        given(createdLoan.getContentUrlFor(any(String.class))).willReturn(CONTENT_URL);
        given(createdLoan.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(createdLoan.getLoanId()).willReturn(CP_LOAN_ID);
        given(elibFacade.createLoan(any(ContentProviderConsumer.class), any(String.class), any(String.class))).willReturn(createdLoan);
    }

    private void givenCreatedDownloadableContent() {
        given(contentFactory.create(any(String.class), any(FormatDecoration.class))).willReturn(downloadableContent);
    }

    private void whenExecute() {
        actualLoan = underTest.execute(commandData);
    }

    private void thenActualLoanEqualsToExpectedLoan() {
        assertThat(actualLoan.getContent(), is(loan.getContent()));
        assertThat(actualLoan.getExpirationDate(), is(loan.getExpirationDate()));
        assertThat(actualLoan.getMetadata().getContentProvider(), is(loan.getMetadata().getContentProvider()));
        assertThat(actualLoan.getMetadata().getExpirationDate(), is(loan.getMetadata().getExpirationDate()));
        assertThat(actualLoan.getMetadata().getFormatDecoration(), is(loan.getMetadata().getFormatDecoration()));
        assertThat(actualLoan.getMetadata().getId(), is(loan.getMetadata().getId()));
        assertThat(actualLoan.getMetadata().getRecordId(), is(loan.getMetadata().getRecordId()));
    }
}
