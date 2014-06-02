package com.axiell.ehub.provider;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;

@RunWith(MockitoJUnitRunner.class)
public class ContentProviderDataAccessorFacadeTest {
    private IContentProviderDataAccessorFacade underTest;
    @Mock
    private IContentProviderDataAccessorFactory contentProviderDataAccessorFactory;
    @Mock
    private IContentProviderDataAccessor contentProviderDataAccessor;
    @Mock
    private EhubConsumer ehubConsumer;
    @Mock
    private PendingLoan pendingLoan;
    @Mock
    private EhubLoan ehubLoan;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private ContentProviderLoan contentProviderLoan;
    @Mock
    private IContent content;

    @Before
    public void setUpContentProviderDataAccessorFacade() {
        underTest = new ContentProviderDataAccessorFacade();
        ReflectionTestUtils.setField(underTest, "contentProviderDataAccessorFactory", contentProviderDataAccessorFactory);
    }

    @Before
    public void setUpContentProviderDataAccessor() {
        given(contentProviderDataAccessorFactory.getInstance(ContentProviderName.ELIB)).willReturn(contentProviderDataAccessor);
    }

    @Before
    public void setUpPendingLoan() {
        given(pendingLoan.getContentProviderNameEnum()).willReturn(ContentProviderName.ELIB);
    }

    @Before
    public void setUpEhubLoan() {
        given(ehubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
        given(contentProviderLoanMetadata.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(ContentProviderName.ELIB);
    }

    @Before
    public void setUpContentProviderLoan() {
        given(contentProviderDataAccessor.createLoan(any(ContentProviderConsumer.class), any(String.class), any(String.class), any(PendingLoan.class), any(String.class))).willReturn(
                contentProviderLoan);
    }

    @Before
    public void setUpContent() {
        given(contentProviderDataAccessor.getContent(any(ContentProviderConsumer.class), any(String.class), any(String.class), any(ContentProviderLoanMetadata.class), any(String.class))).willReturn(content);
    }

    @Test
    public void createLoan() {
        whenCreateLoan();
        thenLoanIsCreatedByContentProvider();
    }

    private void whenCreateLoan() {
        underTest.createLoan(ehubConsumer, "libraryCard", "pin", pendingLoan, "language");
    }

    private void thenLoanIsCreatedByContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(ContentProviderName.class));
        inOrder.verify(contentProviderDataAccessor).createLoan(any(ContentProviderConsumer.class), any(String.class), any(String.class), any(PendingLoan.class), any(String.class));
    }

    @Test
    public void getContent() {
        whenGetContent();
        thenContentIsRetrievedFromContentProvider();
    }

    private void whenGetContent() {
        underTest.getContent(ehubConsumer, ehubLoan, "libraryCard", "pin", "language");
    }

    private void thenContentIsRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(ContentProviderName.class));
        inOrder.verify(contentProviderDataAccessor).getContent(any(ContentProviderConsumer.class), any(String.class), any(String.class), any(ContentProviderLoanMetadata.class), any(String.class));
    }

    @Test
    public void getFormats() {
        whenGetFormats();
        thenFormatsAreRetrievedFromContentProvider();
    }

    private void whenGetFormats() {
        underTest.getFormats(ehubConsumer, ContentProviderName.ELIB.toString(), "card", "contentProviderRecordId", "language");
    }

    private void thenFormatsAreRetrievedFromContentProvider() {
        InOrder inOrder = inOrder(contentProviderDataAccessorFactory, contentProviderDataAccessor);
        inOrder.verify(contentProviderDataAccessorFactory).getInstance(any(ContentProviderName.class));
        inOrder.verify(contentProviderDataAccessor).getFormats(any(ContentProviderConsumer.class), any(String.class), any(String.class), any(String.class));
    }
}
