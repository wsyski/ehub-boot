package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.ContentBuilder;
import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.checkout.SupplementLinkBuilder;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;

import static com.axiell.ehub.checkout.ContentLinkMatcher.matchesExpectedContentLink;
import static com.axiell.ehub.checkout.SupplementLinkMatcher.matchesExpectedSupplementLink;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CreateLoanCommandChainTest {
    private static final Date EXPIRATION_DATE = new Date();
    private static final String CP_LOAN_ID = "CP_LOAN_ID";
    private static final String CP_RECORD_ID = "CP_RECORD_ID";
    private static final String CP_FORMAT_ID = "contentProviderFormatId";

    private CreateLoanCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
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
    private Patron patron;
    @Mock
    private GetLoansResponse getLoansResponse;
    @Mock
    private LoanDTO loan;

    private ContentProviderLoan actualLoan;

    @Before
    public void setUpUnderTest() {
        underTest = new CreateLoanCommandChain(elibFacade, exceptionFactory);
    }

    @Before
    public void setUpContentProviderLoanWithDefaultData() {
        given(contentProviderLoan.expirationDate()).willReturn(EXPIRATION_DATE);
        given(contentProviderLoan.content()).willReturn(ContentBuilder.contentWithSupplementLinks());
        given(loanMetadata.getContentProvider()).willReturn(contentProvider);
        given(loanMetadata.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(loanMetadata.getFirstFormatDecoration()).willReturn(formatDecoration);
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
        whenExecute();
        thenActualLoanEqualsToExpectedLoan();
    }

    @Test
    public void execute_patronHasLoanWithProductId() {
        givenLoanWithProductIdInGetLoansResponse();
        givenGetLoansResponse();
        givenCommandData();
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
        given(pendingLoan.contentProviderFormatId()).willReturn(CP_FORMAT_ID);
        commandData = CommandData.newInstance(contentProviderConsumer, patron, "sv").setPendingLoan(pendingLoan);
    }

    private void givenProductIsAvailable() {
        given(bookAvailability.isProductAvailable(any(String.class))).willReturn(true);
        given(elibFacade.getBookAvailability(any(ContentProviderConsumer.class), any(String.class), any(Patron.class))).willReturn(bookAvailability);
    }

    private void givenCreatedLoan() {
        given(createdLoan.getContentUrlsFor(any(String.class))).willReturn(Collections.singletonList(ContentLinkBuilder.HREF));
        given(createdLoan.getSupplements()).willReturn(getSupplements());
        given(createdLoan.getContentUrlsFor(CP_FORMAT_ID)).willReturn(ContentLinkBuilder.defaultContentLinks().hrefs());
        given(createdLoan.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(createdLoan.getLoanId()).willReturn(CP_LOAN_ID);
        given(elibFacade.createLoan(any(ContentProviderConsumer.class), any(String.class), any(Patron.class))).willReturn(createdLoan);
    }

    private void whenExecute() {
        actualLoan = underTest.execute(commandData);
    }

    private void thenActualLoanEqualsToExpectedLoan() {
        Assert.assertThat(actualLoan.content().getContentLinks().getContentLinks().get(0),
                matchesExpectedContentLink(contentProviderLoan.content().getContentLinks().getContentLinks().get(0)));
        Assert.assertThat(actualLoan.content().getSupplementLinks().getSupplementLinks().get(0),
                matchesExpectedSupplementLink(contentProviderLoan.content().getSupplementLinks().getSupplementLinks().get(0)));
        assertThat(actualLoan.expirationDate(), is(contentProviderLoan.expirationDate()));
        assertThat(actualLoan.getMetadata().getContentProvider(), is(contentProviderLoan.getMetadata().getContentProvider()));
        assertThat(actualLoan.getMetadata().getExpirationDate(), is(contentProviderLoan.getMetadata().getExpirationDate()));
        assertThat(actualLoan.getMetadata().getFirstFormatDecoration(), is(contentProviderLoan.getMetadata().getFirstFormatDecoration()));
        assertThat(actualLoan.getMetadata().getId(), is(contentProviderLoan.getMetadata().getId()));
        assertThat(actualLoan.getMetadata().getRecordId(), is(contentProviderLoan.getMetadata().getRecordId()));
    }

    private void givenLoanWithProductIdInGetLoansResponse() {
        given(loan.getLoanId()).willReturn(CP_LOAN_ID);
        given(loan.getProductId()).willReturn(CP_RECORD_ID);
        given(loan.getExpirationDate()).willReturn(EXPIRATION_DATE);
        given(loan.getSupplements()).willReturn(getSupplements());
        given(loan.getContentUrlsFor(CP_FORMAT_ID)).willReturn(ContentLinkBuilder.defaultContentLinks().hrefs());
        given(getLoansResponse.getLoanWithProductId(anyString())).willReturn(loan);
    }

    private Supplements getSupplements() {
        Supplements supplements = new Supplements();
        SupplementDTO supplementDTO = new SupplementDTO().name(SupplementLinkBuilder.NAME).href(SupplementLinkBuilder.HREF);
        supplements.add(supplementDTO);
        return supplements;
    }
}
