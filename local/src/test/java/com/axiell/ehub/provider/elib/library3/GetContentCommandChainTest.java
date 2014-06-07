package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentFactory;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GetContentCommandChainTest {

    private static final String LOAN_ID = "loanId";
    private static final String CONTENT_URL = "contentUrl";
    private GetContentCommandChain underTest;
    @Mock
    private IElibFacade elibFacade;
    @Mock
    private IEhubExceptionFactory exceptionFactory;
    @Mock
    private IContentFactory contentFactory;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private ContentProviderLoanMetadata loanMetadata;
    @Mock
    private Loan loan;
    @Mock
    private DownloadableContent downloadableContent;
    private CommandData data;
    private IContent actualContent;

    @Before
    public void setUpUnderTest() {
        underTest = new GetContentCommandChain(elibFacade, exceptionFactory, contentFactory);
    }

    @Test
    public void execute() {
        givenCommandData();
        givenActiveLoanWithContentUrl();
        givenExpectedContent();
        whenExecute();
        thenActualContentEqualsExpectedContent();
    }

    private void givenActiveLoanWithContentUrl() {
        given(loan.isActive()).willReturn(true);
        given(loan.getFirstContentUrl()).willReturn(CONTENT_URL);
        given(elibFacade.getLoan(any(ContentProviderConsumer.class), any(String.class))).willReturn(loan);
    }

    private void givenExpectedContent() {
        given(downloadableContent.getUrl()).willReturn(CONTENT_URL);
        given(contentFactory.create(any(String.class), any(FormatDecoration.class))).willReturn(downloadableContent);
    }

    private void whenExecute() {
        actualContent = underTest.execute(data);
    }

    private void thenActualContentEqualsExpectedContent() {
        DownloadableContent actualDownloadableContent = (DownloadableContent) actualContent;
        Assert.assertEquals(CONTENT_URL, actualDownloadableContent.getUrl());
    }

    private void givenCommandData() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(loanMetadata.getId()).willReturn(LOAN_ID);
        data = CommandData.newInstance(contentProviderConsumer, "libraryCard").setContentProviderLoanMetadata(loanMetadata);
    }
}
