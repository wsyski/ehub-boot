package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GetContentCommandChainTest {
    private static final String LOAN_ID = "loanId";
    private GetContentCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private ContentProviderLoanMetadata loanMetadata;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private LoanDTO loan;
    private CommandData data;
    private ContentLink actualContentLink;

    @Before
    public void setUpUnderTest() {
        underTest = new GetContentCommandChain(elibFacade, exceptionFactory);
    }

    @Test
    public void execute() {
        givenCommandData();
        givenActiveLoanWithContentUrl();
        whenExecute();
        thenActualContentEqualsExpectedContent();
    }

    private void givenActiveLoanWithContentUrl() {
        given(loan.isActive()).willReturn(true);
        given(loan.getContentUrlsFor(any(String.class))).willReturn(Collections.singletonList(ContentLinkBuilder.HREF));
        given(elibFacade.getLoan(any(ContentProviderConsumer.class), any(String.class))).willReturn(loan);
    }

    private void whenExecute() {
        actualContentLink = underTest.execute(data).getContentLinks().getContentLinks().get(0);
    }

    private void thenActualContentEqualsExpectedContent() {
        Assert.assertEquals(ContentLinkBuilder.HREF, actualContentLink.href());
    }

    private void givenCommandData() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(loanMetadata.getId()).willReturn(LOAN_ID);
        data = CommandData.newInstance(contentProviderConsumer, new Patron.Builder().libraryCard("card").pin("pin").build(), "sv").setContentProviderLoanMetadata(loanMetadata).setFormatDecoration(formatDecoration);
    }
}
